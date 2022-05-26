package com.example.androidexample.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.androidexample.domain.JokesUseCase
import com.example.androidexample.domain.models.DomainObject
import com.example.androidexample.presentation.mapper.PresentationModelMapper
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
class MainViewModel @Inject constructor(
    private val useCase: JokesUseCase,
    private val mapper: PresentationModelMapper
) : ViewModel() {

    companion object {
        private const val TAG = "MainViewModel"
    }

    val model: Observable<PresentationModel>

    private val events = BehaviorSubject.create<Event>()
    private val states = BehaviorSubject.create<State>()

    private val initialState = State(
        data = emptyList(),
        loading = false,
        error = false
    )

    private val onclickRetry: () -> Unit = {
        Log.i(TAG, "On click retry button was triggered")

        events.onNext(Event.OnOpen)
    }

    init {
        model = events
            .startWithItem(Event.OnOpen)
            .publish { event ->
                Observable.mergeArray(
                    event.ofType(Event.OnOpen::class.java)
                        .withLatestFrom(
                            states.startWithItem(initialState)
                        ) { _, state -> state }
                        .switchMap { state ->
                            if (state.data.isEmpty()) {
                                useCase.getData()
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
                            } else {
                                Observable.just(state)
                            }
                        }
                )
            }
            .doOnNext { state ->
                states.onNext(state)
            }
            .map { state ->
                mapper.transform(
                    state = state,
                    onclickRetry = onclickRetry
                )
            }
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
