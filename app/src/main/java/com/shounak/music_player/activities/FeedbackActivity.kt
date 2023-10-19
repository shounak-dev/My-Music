package com.shounak.music_player

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.shounak.music_player.databinding.ActivityFeedbackBinding
import org.json.JSONException
import org.json.JSONObject


class FeedbackActivity : AppCompatActivity() {

    lateinit var binding: ActivityFeedbackBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedbackBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Feedback"
        binding.sendFA.setOnClickListener {
            val feedbackMsg = binding.feedbackMsgFA.text.toString()
            val sender = binding.emailFA.text.toString()
            val subject = binding.topicFA.text.toString()

            if (feedbackMsg.isNotEmpty() && subject.isNotEmpty()) {
                sendFeedback(sender, subject, feedbackMsg, this)
            } else Toast.makeText(this, "Please, fill out the required fields", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun sendFeedback(
        sender: String,
        subject: String,
        message: String,
        mContext: android.content.Context
    ) {
        // Send a POST request to Github to launch the action
        // https://docs.github.com/en/rest/reference/actions#create-a-workflow-dispatch-event
        // Parameters:
        // - sender: the github account of the sender
        // - subject: the subject of the feedback
        // - message: the message of the feedback
        // - mContext: the context of the activity

        val url = "https://api.github.com/repos/yagueto/Music/dispatches"
        val requstQueue = Volley.newRequestQueue(mContext)
        val token = "" // TODO: Add your token here.
        // See https://docs.github.com/en/rest/reference/actions#create-a-workflow-dispatch-event

        val jsonBody = JSONObject()
        jsonBody.put("event_type", "webhook-run")
        val data = JSONObject()
        data.put("sender", sender)
        data.put("subject", subject)
        data.put("message", message)
        jsonBody.put("client_payload", data)

        val req = object : JsonObjectRequest(Request.Method.POST, url, jsonBody, { _ ->
            Toast.makeText(mContext, "Feedback Sent Successfully", Toast.LENGTH_SHORT).show()
        }, { error ->
            val response = error.networkResponse
            if (response?.data != null) {
                val json = String(response.data)
                try {
                    val obj = JSONObject(json)
                    val trimmedString = obj.getString("message")
                    Toast.makeText(mContext, trimmedString, Toast.LENGTH_SHORT).show()
                    Log.e("Feedback", trimmedString)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/vnd.github.everest-preview+json"
                headers["Authorization"] = "token $token"
                return headers
            }
        }
        requstQueue.add(req)
    }
}