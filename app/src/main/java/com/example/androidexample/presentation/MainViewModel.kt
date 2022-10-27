package com.example.androidexample.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.androidexample.domain.JokesUseCase
import com.example.androidexample.domain.models.Joke
import com.example.androidexample.presentation.mapper.MainUiModelMapper
import com.example.androidexample.presentation.models.JokeUiModel
import com.example.androidexample.presentation.models.MainUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

/**
 * The parameters of an annotated constructor of a class are the dependencies of that class.
 * In this case, MainViewModel has useCase and mapper as dependencies.
 * Therefore, Hilt must also know how to provide the instance of
 * JokesUseCase and PresentationModelMapper.
 * */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val useCase: JokesUseCase,
    private val mapper: MainUiModelMapper
) : ViewModel() {

    companion object {
        private const val TAG = "MainViewModel"
    }

    val uiModel: Observable<MainUiModel>

    private val events = BehaviorSubject.create<Event>()
    private val states = BehaviorSubject.create<State>()

    private val initialState = State(
        data = emptyList(),
        loading = false,
        error = false,
        selected = null
    )

    private val onClickRetry: () -> Unit = {
        Log.i(TAG, "On click retry button was triggered")

        events.onNext(Event.OnOpen)
    }

    private val onClickItem: (JokeUiModel) -> Unit = { item ->
        Log.i(TAG, "On click item was triggered")

        events.onNext(Event.OnClickItem(item))
    }

    private val onCloseItemDetails: () -> Unit = {
        Log.i(TAG, "On close item details was triggered")

        events.onNext(Event.OnCloseItemDetails)
    }

    private val onSwipeRefresh: () -> Unit = {
        Log.i(TAG, "On swipe refresh was triggered")

        events.onNext(Event.OnSwipeRefresh)
    }

    init {
        uiModel = events
            .startWithItem(Event.OnOpen)
            .publish { publishedEvent ->
                Observable.mergeArray(
                    publishedEvent.ofType(Event.OnOpen::class.java)
                        .withLatestFrom(states.startWithItem(initialState)) { _, state -> state }
                        .switchMap { state ->
                            if (state.data.isEmpty()) {
                                getData(state)
                            } else {
                                Observable.just(state)
                            }
                        },

                    publishedEvent.ofType(Event.OnClickItem::class.java)
                        .withLatestFrom(states) { event, state -> event to state }
                        .map { (event, state) -> state.selected(event.item) },

                    publishedEvent.ofType(Event.OnCloseItemDetails::class.java)
                        .withLatestFrom(states) { _, state -> state }
                        .map { state -> state.unselect() },

                    publishedEvent.ofType(Event.OnSwipeRefresh::class.java)
                        .withLatestFrom(states) { _, state -> state }
                        .switchMap { state -> getData(state) }
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

    private fun getData(state: State): Observable<State> {
        return useCase.getData()
            .toObservable()
            .map { data ->
                Log.i(TAG, "Event.OnOpen: Data fetched successfully")

                state.setData(data)
            }
            .startWithItem(state.loading())
            .onErrorReturn { error ->
                Log.e(TAG, "Event.OnOpen: Failed to fetch the data: $error")

                state.error()
            }
    }

    sealed class Event {
        object OnOpen : Event()
        data class OnClickItem(val item: JokeUiModel) : Event()
        object OnCloseItemDetails : Event()
        object OnSwipeRefresh : Event()
    }

    data class State(
        val data: List<Joke>,
        val loading: Boolean,
        val error: Boolean,
        val selected: JokeUiModel?
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

        fun selected(item: JokeUiModel) = this.copy(selected = item)
        fun unselect() = this.copy(selected = null)
    }
}
