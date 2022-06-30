package rs.raf.jun.presentation.view.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import org.koin.android.ext.android.bind
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import rs.raf.jun.R
import rs.raf.jun.databinding.FragmentProfileBinding
import rs.raf.jun.presentation.contract.MainContract
import rs.raf.jun.presentation.view.activities.LoginActivity
import rs.raf.jun.presentation.view.states.LocalUserState
import rs.raf.jun.presentation.viewmodel.MainViewModel

class ProfileFragment  : Fragment(R.layout.fragment_profile){

    private val mainViewModel: MainContract.ViewModel by sharedViewModel<MainViewModel>()
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private var logoutBtn: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        initView()
        initListeners()
    }

    private fun initView() {
        this.logoutBtn = binding.logoutBtn
        val pref = context?.getSharedPreferences(requireContext().packageName, Activity.MODE_PRIVATE)
        val email = pref?.getString(LoginActivity.SharedPrefKeys.EMAIL_KEY, null)
        val username = pref?.getString(LoginActivity.SharedPrefKeys.USERNAME_KEY, null)
        binding.emailTv.text = email
        binding.usernameTv.text = username
    }

    private fun initListeners() {
        this.logoutBtn?.setOnClickListener {
            val pref = activity?.applicationContext?.getSharedPreferences(
                activity?.packageName,
                AppCompatActivity.MODE_PRIVATE
            )
            val editor = pref?.edit()
            editor?.putString(LoginActivity.SharedPrefKeys.CREDENTIALS_KEY, "loggedOut")
            editor?.apply()

            val intent = Intent(this.activity, LoginActivity::class.java)
            activity?.startActivity(intent)

            activity?.finish()
        }
    }
}