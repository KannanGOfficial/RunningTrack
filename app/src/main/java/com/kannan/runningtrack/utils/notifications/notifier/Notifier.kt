package com.kannan.runningtrack.utils.notifications.notifier

interface Notifier {
    fun postNotification()
    fun postNotification(contextText : String)
}