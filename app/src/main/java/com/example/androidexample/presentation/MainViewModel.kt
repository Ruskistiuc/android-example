package com.example.androidexample.presentation

import androidx.lifecycle.ViewModel
import com.example.androidexample.domain.JokesUseCase
import com.example.androidexample.domain.models.Joke
import com.example.androidexample.presentation.mapper.ScreenStateMapper
import com.example.androidexample.presentation.models.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val useCase: JokesUseCase,
    private val mapper: ScreenStateMapper,
) : ViewModel() {

    val uiModel: Observable<ScreenState>

    private val events = PublishSubject.create<Event>()
    private val states = BehaviorSubject.create<InnerState>()

    private val initialState = InnerState(
        data = emptyList(),
        loading = false,
        error = false,
        selected = null,
    )

    private val onClickRetry: () -> Unit = { events.onNext(Event.OnFetchData) }
    private val onSwipeRefresh: () -> Unit = { events.onNext(Event.OnFetchData) }
    private val onClickItem: (Joke) -> Unit = { events.onNext(Event.OnClickItem(it)) }
    private val onCloseItemDetails: () -> Unit = { events.onNext(Event.OnCloseItemDetails) }

    init {
        uiModel = events
            .startWithItem(Event.OnOpen)
            .publish { publishedEvent ->
                Observable.mergeArray(
                    publishedEvent.ofType(Event.OnOpen::class.java)
                        .withLatestFrom(states.startWithItem(initialState), ::Pair)
                        .switchMap { (_, state) ->
                            if (state.data.isEmpty()) {
                                getData(state)
                            } else {
                                Observable.just(state)
                            }
                        },

                    publishedEvent.ofType(Event.OnFetchData::class.java)
                        .withLatestFrom(states, ::Pair)
                        .switchMap { (_, state) -> getData(state) },

                    publishedEvent.ofType(Event.OnClickItem::class.java)
                        .withLatestFrom(states, ::Pair)
                        .map { (event, state) -> state.selected(event.item) },

                    publishedEvent.ofType(Event.OnCloseItemDetails::class.java)
                        .withLatestFrom(states, ::Pair)
                        .map { (_, state) -> state.unselect() },
                )
            }
            .doOnNext { state -> states.onNext(state) }
            .map { state ->
                mapper.transform(
                    state = state,
                    onClickRetry = onClickRetry,
                    onClickItem = onClickItem,
                    onCloseItemDetails = onCloseItemDetails,
                    onSwipeRefresh = onSwipeRefresh
                )
            }
    }

    private fun getData(state: InnerState): Observable<InnerState> {
        return useCase.getData()
            .toObservable()
            .map { data -> state.setData(data) }
            .startWithItem(state.loading())
            .onErrorReturn { state.error() }
    }

    sealed class Event {
        object OnOpen : Event()
        object OnFetchData : Event()
        data class OnClickItem(val item: Joke) : Event()
        object OnCloseItemDetails : Event()
    }

    data class InnerState(
        val data: List<Joke>,
        val loading: Boolean,
        val error: Boolean,
        val selected: Joke?
    ) {
        fun setData(data: List<Joke>) = this.copy(
            data = data, loading = false, error = false, selected = null
        )

        fun loading() = this.copy(
            data = emptyList(), loading = true, error = false, selected = null
        )

        fun error() = this.copy(
            data = emptyList(), loading = false, error = true, selected = null
        )

        fun selected(item: Joke) = this.copy(selected = item)

        fun unselect() = this.copy(selected = null)
    }
}
