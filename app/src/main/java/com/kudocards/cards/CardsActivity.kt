package com.kudocards.cards

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.TextView
import com.kudocards.R
import com.kudocards.data.models.Card
import com.kudocards.data.remote.FirebaseConnectionImpl
import com.kudocards.send.SendActivity
import com.kudocards.ui.KudoListAdapter
import com.kudocards.util.ImageMapperUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_main.*
import com.kudocards.splash.SplashActivity
import com.firebase.ui.auth.AuthUI



class CardsActivity : AppCompatActivity(), CardsContract.View {

    override lateinit var presenter: CardsContract.Presenter

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.change_team -> {
                showChangeTeamDialog()
                true
            }
            R.id.do_logout -> {
                logoutDialog()
                true
            }
            else ->
                super.onOptionsItemSelected(item)
        }
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_team -> {
                presenter.getTeamKudoCards()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_received -> {
                presenter.getReceivedKudoCards()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_sent -> {
                presenter.getSentKudoCards()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        send_kudo.setOnClickListener {startSendKudoCardsActivity()}
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        kudo_cards_recyclerview.layoutManager = LinearLayoutManager(this)

        val dataBase = FirebaseDatabase.getInstance().reference
        val firebaseConnection = FirebaseConnectionImpl(dataBase)
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val deviceToken = FirebaseInstanceId.getInstance().token

        CardsPresenter(firebaseConnection, firebaseUser, deviceToken.toString(), this)
    }

    override fun onResume() {
        super.onResume()

        if (navigation.selectedItemId == R.id.navigation_team) {
            presenter.start()
        }
    }

    override fun showLoading() {
        kudo_cards_recyclerview.visibility = GONE
        load_cards_progress.visibility = VISIBLE
    }

    override fun hideLoading() {
        kudo_cards_recyclerview.visibility = VISIBLE
        load_cards_progress.visibility = GONE
    }

    override fun showErrorDialog() {
        val alertDialog = AlertDialog.Builder(this@CardsActivity).create()
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

    override fun loadTeamCards(teamCards: MutableList<Card>) {
        updateKudoList(teamCards)
    }

    override fun loadReceivedCards(receivedCards: MutableList<Card>) {
        updateKudoList(receivedCards)
    }

    override fun loadSentCards(sentCards: MutableList<Card>) {
        updateKudoList(sentCards)
    }

    private fun updateKudoList(cards : List<Card>) {
        val adapter = KudoListAdapter(cards) {
            startCardDialog(it)
        }
        kudo_cards_recyclerview.adapter = adapter
    }


    private fun startCardDialog(card: Card) {
        val dialog = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.dialog_card, null)
        ImageMapperUtils.getImageResource(card.type)?.let { dialogView.findViewById<ImageView>(R.id.cardImage).setImageResource(it) }

        dialogView.findViewById<TextView>(R.id.titleText).text = card.title
        dialogView.findViewById<TextView>(R.id.messageText).text = card.message
        dialogView.findViewById<TextView>(R.id.fromText).text = String.format(getString(R.string.from), card.senderDisplayName)
        dialogView.findViewById<TextView>(R.id.toText).text = String.format(getString(R.string.to), card.destinationDisplayName)
        dialogView.findViewById<TextView>(R.id.dateTimeText).text = card.creationDate
        dialog.setView(dialogView)
        dialog.setCancelable(false)
        dialog.setPositiveButton(R.string.ok_text, { _: DialogInterface, _: Int -> })
        dialog.show()
    }

    private fun startSendKudoCardsActivity () {
        val sendKudoCard = Intent(this, SendActivity::class.java)
        startActivity(sendKudoCard)
    }

    override fun showChangeTeamDialog() {
        val alertDialog = AlertDialog.Builder(this@CardsActivity).create()
        alertDialog.setTitle(getString(R.string.change_user_team))
        alertDialog.setMessage(getString(R.string.type_new_team))

        val view = View.inflate(this, R.layout.layout_change_team, null)
        alertDialog.setView(view)

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.ok_text)) {
            dialog, _ ->

            val text = alertDialog.findViewById<TextView>(R.id.new_team_name_text)!!.text
            if (!text.isEmpty()) {
                presenter.changeUserTeam(text.toString())
                dialog.dismiss()
            }
        }

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel_text)) {
            dialog, _ ->
            dialog.dismiss()
        }

        alertDialog.show()
    }

    override fun reloadCards() {
        navigation.menu.getItem(0).isChecked = true
        presenter.getTeamKudoCards()
    }

    override fun logoutDialog() {
        val alertDialog = AlertDialog.Builder(this@CardsActivity).create()
        alertDialog.setTitle(getString(R.string.do_logout))
        alertDialog.setMessage(getString(R.string.logout_dialog_message))

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.ok_text)) {
            dialog, _ ->
            run {
                alertDialog.dismiss()
                showLoading()
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener {
                            hideLoading()
                            val splash = Intent(this, SplashActivity::class.java)
                            startActivity(splash)
                        }
            }
        }

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel_text)) {
            dialog, _ ->
            dialog.dismiss()
        }

        alertDialog.show()
    }
}
