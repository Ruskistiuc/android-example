package com.example.androidexample.presentation

import androidx.lifecycle.ViewModel
import com.example.androidexample.domain.UseCase
import com.example.androidexample.domain.models.DomainObject
import com.example.androidexample.presentation.models.PresentationItemModel
import com.example.androidexample.presentation.models.PresentationModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

/**
 * The parameters of an annotated constructor of a class are the dependencies of that class.
 * In this case, MainViewModel has useCase as a dependency.
 * Therefore, Hilt must also know how to provide the instance of UseCase.
 * */
@HiltViewModel
class MainViewModel @Inject constructor(useCase: UseCase) : ViewModel() {

    val model: Observable<PresentationModel>

    private val events = BehaviorSubject.create<Event>()
    private val states = BehaviorSubject.create<State>()

    private val initialState = State(
        data = emptyList(),
        loading = false,
        error = false
    )

    init {
        model = events
            .startWithItem(Event.OnOpen)
            .publish { event ->
                event.ofType(Event.OnOpen::class.java)
                    .withLatestFrom(
                        states.startWithItem(initialState)
                    ) { _, state -> state }
                    .switchMap { state ->
                        if (state.data.isEmpty()) {
                            useCase.getData()
                                .toObservable()
                                .map { data ->
                                    state.setData(data)
                                }
                                .startWithItem(state.loading())
                                .onErrorReturn { state.error() }
                        } else {
                            Observable.just(state)
                        }
                    }
            }
            .doOnNext { state ->
                states.onNext(state)
            }
            .map { state ->
                getModel(state)
            }
    }

    private fun getModel(state: State): PresentationModel {
        return PresentationModel(
            items = state.data.map { item ->
                PresentationItemModel(
                    setup = item.setup,
                    punchline = item.punchline
                )
            },
            isLoading = state.loading,
            isError = state.error
        )
    }

    sealed class Event {
        object OnOpen : Event()
    }

    data class State(
        val data: List<DomainObject>,
        val loading: Boolean,
        val error: Boolean
    ) {
        fun setData(data: List<DomainObject>) = this.copy(
            data = data, loading = false, error = false
        )

        fun loading() = this.copy(loading = true, error = false)
        fun error() = this.copy(loading = false, error = true)
    }
}
