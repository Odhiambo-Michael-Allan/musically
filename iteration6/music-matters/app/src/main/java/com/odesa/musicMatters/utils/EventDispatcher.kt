package com.odesa.musicMatters.utils

typealias EventSubscriber<T> = ( T ) -> Unit
typealias EventUnsubscribeFunction = () -> Unit

class EventDispatcher<T> {
    private val subscribers = mutableListOf<EventSubscriber<T>>()

    fun subscribe( subscriber: EventSubscriber<T> ): EventUnsubscribeFunction {
        subscribers.add( subscriber )
        return { unsubscribe( subscriber ) }
    }

    fun unsubscribe( subscriber: EventSubscriber<T> ) {
        subscribers.remove( subscriber )
    }

    fun dispatchEvent(event: T ) {
        subscribers.forEach { it( event ) }
    }

}