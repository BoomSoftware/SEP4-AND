package com.example.sep4_android.views.login;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.sep4_android.MainAppActivity;
import com.example.sep4_android.R;
import com.example.sep4_android.viewmodels.SignInViewModel;
import com.firebase.ui.auth.AuthUI;
import java.util.Arrays;
import java.util.List;
import es.dmoral.toasty.Toasty;

import static android.app.Activity.RESULT_OK;

public class SignInFragment extends Fragment {

    private final int RC_SIGN_IN = 1;
    private View view;
    private SignInViewModel loginViewModel;
    private Button loginButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        loginViewModel = new ViewModelProvider(this).get(SignInViewModel.class);
        view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        checkIfSignedIn();
        prepareUI();
        prepareOnClickActions();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN) {
            handleSignInRequest(resultCode);
        }
    }

    private void checkIfSignedIn(){
        loginViewModel.getCurrentUser().observe(getViewLifecycleOwner(), firebaseUser -> {
            if(firebaseUser != null){
                loginViewModel.getStatus().observe(getViewLifecycleOwner(), status -> {
                    if(status != null && status){
                        Intent intent = new Intent(getContext(), MainAppActivity.class);
                        getContext().startActivity(intent);
                    }else{
                        Navigation.findNavController(view).navigate(R.id.action_signInFragment_to_signUpFragment);
                    }
                });
            }
        });
    }

    private void prepareUI() {
        loginButton = view.findViewById(R.id.button_login);
    }

    private void prepareOnClickActions() {
        loginButton.setOnClickListener(v -> {
            signIn();
        });
    }

    private void signIn(){
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build();

        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInRequest(int resultCode){
        if(resultCode == RESULT_OK) {
            loginViewModel.getStatus().observe(getActivity(), status -> {
                if(status != null && status){
                    Intent intent = new Intent(getContext(), MainAppActivity.class);
                    getContext().startActivity(intent);
                }
            });

        }
        else{
            Toasty.error(getContext(), getContext().getString(R.string.invalid_auth), Toasty.LENGTH_SHORT).show();
        }
    }
}