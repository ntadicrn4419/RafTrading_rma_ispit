package rs.raf.jun.presentation.view.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import rs.raf.jun.R
import rs.raf.jun.application.RafTradingApp
import rs.raf.jun.data.models.user.User
import rs.raf.jun.presentation.contract.MainContract
import rs.raf.jun.presentation.view.states.LocalUserState
import rs.raf.jun.presentation.viewmodel.MainViewModel

class LoginActivity : AppCompatActivity() {

    private val mainViewModel: MainContract.ViewModel by viewModel<MainViewModel>()
    private var allRegistratedUsers: List<User>? = null

    object SharedPrefKeys{
        const val CREDENTIALS_KEY = "credentials_key"
        const val USERNAME_KEY = "username_key"
        const val PASSWORD_KEY = "password_key"
        const val EMAIL_KEY = "email_key"
        const val ACCOUNT_BALANCE_KEY = "account_balance_key"
        const val PORTFOLIO_VALUE_KEY = "portfolio_value_key"
    }

    private val definedUsername = "miki"
    private val definedEmail = "miki@gmail.com"
    private val definedPassword = "mikimiki"

    private var loginBtn: Button? = null
    private var usernameEt: EditText? = null
    private var emailEt: EditText? = null
    private var passwordEt: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        init()
    }

    private fun init() {
        initView()
        initObserver()
        initListeners()
    }

    private fun initView() {
        this.loginBtn = findViewById(R.id.login_btn)
        this.usernameEt = findViewById(R.id.username_et)
        this.emailEt = findViewById(R.id.email_et)
        this.passwordEt = findViewById(R.id.password_et)
    }

    private fun initListeners() {
        this.loginBtn?.setOnClickListener {

            val uname = usernameEt!!.text.toString()
            val email = emailEt!!.text.toString()
            val password = passwordEt!!.text.toString()

            if (checkCredentials(uname, email, password)) {
                //Ako postoji u bazi ovaj korisnik ne radimo nista, ako ne postoji dodajemo ga u bazu i cuvamo podatke u sharedPref(kad nam zatrebaju da ne moramo ponovo da citamo iz baze)
                addInDbIfAbsentAndToSharedPref(uname, email, password)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                val toast = Toast.makeText(
                    applicationContext,
                    getErrorMessage(uname, email, password),
                    Toast.LENGTH_SHORT
                )
                toast.show()
            }
        }
    }

    private fun writeToSharedPref(username: String, password: String, email: String, accountBalance: Float, portfolioValue: Float) {
        val pref = applicationContext.getSharedPreferences(packageName, MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString(SharedPrefKeys.CREDENTIALS_KEY, "loggedIn")
        editor.putString(SharedPrefKeys.USERNAME_KEY, username)//
        editor.putString(SharedPrefKeys.EMAIL_KEY, email)
        editor.putString(SharedPrefKeys.PASSWORD_KEY, password)
        editor.putFloat(SharedPrefKeys.ACCOUNT_BALANCE_KEY, accountBalance)
        editor.putFloat(SharedPrefKeys.PORTFOLIO_VALUE_KEY, portfolioValue)
        editor.apply()
    }

    private fun addInDbIfAbsentAndToSharedPref(username: String, email: String, password: String) {
        for(user in allRegistratedUsers!!){
            if (user.username == username && user.email == email){
                writeToSharedPref(username, password, email, user.accountBalance, user.portfolioValue)
                return
            }
        }
        mainViewModel.addUser(User(username, email, password, 10000f, 0f))
        writeToSharedPref(username, password, email, 10000f, 0f)
    }

    private fun initObserver() {
        mainViewModel.localUserState.observe(this){
            renderLocalState(it)
        }
        mainViewModel.getAllUsers()
    }

    private fun renderLocalState(state: LocalUserState) {
        when(state){
            is LocalUserState.GotAllUsers -> {
                allRegistratedUsers = state.users
            }
            is LocalUserState.ErrorState -> {
                Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkCredentials(uname: String, email: String, password: String): Boolean {
        if(uname == definedUsername && email == definedEmail && password == definedPassword){
            return true;
        }
        return false
    }

    //Ukoliko ima vise gresaka, ispisace se samo tekst za jednu gresku. Kad korisnik ispravi tu gresku, ispisace mu se tekst za sledecu gresku
    //i tako dok sve ne popuni kako treba.
    private fun getErrorMessage(uname: String, email: String, password: String): String? {
        var errorMessage: String? = null

        if(password != definedPassword){
            errorMessage = "Wrong password."
        }
        if(password.length < 5){
            errorMessage = "Password must have minimum 5 characters."
        }
        if(email != definedEmail){
            errorMessage = "Wrong email."
        }
        if(uname != definedUsername){
            errorMessage = "Wrong username."
        }
        if(uname == "" || email == "" || password == ""){
            errorMessage = "Input fields must not be empty!"
        }
        return errorMessage
    }

}