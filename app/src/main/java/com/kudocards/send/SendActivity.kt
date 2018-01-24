package com.kudocards.send

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.kudocards.R
import com.kudocards.data.models.Description
import com.kudocards.data.remote.FirebaseConnectionImpl
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.layout_send_kudo_card.*
import android.support.v7.app.AlertDialog
import com.kudocards.util.ImageMapperUtils

/**
 * Created by vitorp on 22/12/17.
 */
class SendActivity : AppCompatActivity(), SendContract.View {
    override lateinit var presenter: SendContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_send_kudo_card)

        val dataBase = FirebaseDatabase.getInstance().reference
        val firebaseConnection = FirebaseConnectionImpl(dataBase)
        SendPresenter(firebaseConnection, this)

        initSendButton()
    }

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    private fun initSendButton() {
        send_kudo_card_button
                .setOnClickListener({ presenter.sendKudoCard(kudo_card_message.text.toString()) })

    }

    override fun showLoading() {
        kudo_card_type_spinner.visibility = GONE
        kudo_card_person_spinner.visibility = GONE
        kudo_card_message.visibility = GONE
        send_kudo_card_button.visibility = GONE
        send_kudo_progress.visibility = VISIBLE
    }

    override fun hideLoading() {
        kudo_card_type_spinner.visibility = VISIBLE
        kudo_card_person_spinner.visibility = VISIBLE
        kudo_card_message.visibility = VISIBLE
        send_kudo_card_button.visibility = VISIBLE
        send_kudo_progress.visibility = GONE
    }

    override fun showSentDialog() {
        val alertDialog = AlertDialog.Builder(this@SendActivity).create()
        alertDialog.setMessage(getString(R.string.sent_kudo_card_success))
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok_text))
        {
            dialog, _ ->
            dialog.dismiss()
            finish()
        }
        alertDialog.show()
    }

    override fun showErrorDialog() {
        val alertDialog = AlertDialog.Builder(this@SendActivity).create()
        alertDialog.setTitle(getString(R.string.attention_alert))
        alertDialog.setMessage(getString(R.string.error_retrieve_data))
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok_text))
        {
            dialog, _ ->
            dialog.dismiss()
            finish()
        }
        alertDialog.show()
    }

    override fun loadUsersSpinner(teamUsersList: MutableList<Description>) {
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, teamUsersList)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        kudo_card_person_spinner.adapter = arrayAdapter

        kudo_card_person_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                presenter.destinationSelected(teamUsersList[position])
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    override fun loadCardTypesSpinner(cardTypes: MutableList<Description>) {
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, cardTypes)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        kudo_card_type_spinner.adapter = arrayAdapter

        kudo_card_type_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                ImageMapperUtils.getImageResource(cardTypes[position].descriptionId)?.let { card_type_image.setImageResource(it) }
                presenter.typeSelected(cardTypes[position])
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }
}