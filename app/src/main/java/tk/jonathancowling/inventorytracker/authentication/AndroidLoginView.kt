package tk.jonathancowling.inventorytracker.authentication


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.content_login.view.*
import kotlinx.android.synthetic.main.fragment_logo_with_card.view.*

import tk.jonathancowling.inventorytracker.R
import tk.jonathancowling.inventorytracker.authentication.services.FirebaseAuthMechanisms
import tk.jonathancowling.inventorytracker.authentication.services.FirebaseAuthService
import tk.jonathancowling.inventorytracker.databinding.ContentLoginBinding

class AndroidLoginView : Fragment() {

    lateinit var vm: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_logo_with_card, container, false)
        .apply {
            val binding = ContentLoginBinding.inflate(inflater, logo_with_card_content, true)
            val observable = EmailPasswordObservable()
            binding.emailPassword = observable
            binding.lifecycleOwner = this@AndroidLoginView

            vm = ViewModelProviders.of(this@AndroidLoginView, AuthViewModel.Factory(observable)).get()
        }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val onBackPressed = OnBackPressedCallback {
            Snackbar.make(view, "please login to continue", Snackbar.LENGTH_SHORT).show()
            true
        }

        activity?.addOnBackPressedCallback(onBackPressed)

        view.login_button_login.setOnClickListener {
            vm.validate().flatMap { model ->
                FirebaseAuthService.Factory().create()
                    .login(FirebaseAuthMechanisms.loginEmailPassword(model.email, model.password))
            }.subscribe({
                findNavController().popBackStack()
            }, {
                Snackbar.make(view, "login failed", Snackbar.LENGTH_SHORT).show()
            })

        }

        view.login_button_signup.setOnClickListener {
            activity?.removeOnBackPressedCallback(onBackPressed)
            findNavController().navigate(R.id.sign_up_destination)
        }
    }
}