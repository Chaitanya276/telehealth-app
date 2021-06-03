package com.app.telehealth.models

class ChattingModel {

    val patientName: String
    val recentMessage: String
    val messageTime: String
    val messageCount: Int

    constructor(
        patientName: String,
        recentMessage: String,
        messageTime: String,
        messageCount: Int
    ) {
        this.patientName = patientName
        this.recentMessage = recentMessage
        this.messageTime = messageTime
        this.messageCount = messageCount
    }
}