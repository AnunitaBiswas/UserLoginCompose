package com.example.userlogincompose.components

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.userlogincompose.viewmodel.AuthViewModel

//@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Login(
   // onSuccessfulLogin: () ->Unit,
    authViewModel: AuthViewModel = viewModel()) {

    val localContext= LocalContext.current

    var userName by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }

    val loginStatus by authViewModel.userLoginStatus.collectAsState()
    var showFailedDialog by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = loginStatus){
        when(loginStatus){
            is AuthViewModel.UserLoginStatus.Failure ->{
                localContext.showToast("Unable to login")
                showFailedDialog=true
            }
            AuthViewModel.UserLoginStatus.Successful ->{
                localContext.showToast("Login successful")
               // onSuccessfulLogin()
            }
            null -> {}
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray)
    ) {
        /*Image(
            painter = painterResource(id = R.drawable.login_bg),
            contentDescription = "Login Background",
            modifier = Modifier
                .fillMaxSize()
                .blur(6.dp),
            contentScale = ContentScale.Crop
        )*/

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(26.dp)
                .alpha(0.7f)
                .clip(
                    CutCornerShape(
                        topStart = 8.dp,
                        topEnd = 16.dp,
                        bottomStart = 16.dp,
                        bottomEnd = 8.dp
                    )
                )
                .background(MaterialTheme.colorScheme.background)
        )

        Column(
            Modifier
                .fillMaxSize()
                .padding(48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {

            LoginHeader()
            LoginFields(userName, password,
                onUsernameChange = { userName = it },
                onPasswordChange = { password = it },
                onForgotPasswordClick = {
                    /* Navigate to next screen */
                } )
            LoginFooter(
                onSignInClick = {
                    when{
                        userName.isBlank() -> {
                            // use error text field
                            localContext.showToast("Enter your username")
                        }
                        password.isBlank() -> {
                            localContext.showToast("Enter your username")
                        }

                        else -> {
                          authViewModel.performLogin(userName,password)
                        }
                    }

                },
                onSignUpClick = {}
            )
        }
    }



    if(showFailedDialog){
        //Alert Dialog
    }
}

@Composable
fun LoginHeader() {
    Column(horizontalAlignment = Alignment.CenterHorizontally){
        Text(text = "Welcome Back", fontSize = 36.sp, fontWeight = FontWeight.ExtraBold)
        Text(text = "Sign in to continue", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
    }


}

@Composable
fun LoginFields(
    username: String, password: String,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onForgotPasswordClick: (String) -> Unit,

) {
   Column() {
       DemoField(value = username, label = "User Name", placeholder = " Enter your email",
           onValueChange = onUsernameChange,
           leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = "Email") },
                   keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next))

       Spacer(modifier = Modifier.height(8.dp))
       DemoField(value = password, label = "Password", placeholder = "Enter your Password",
           onValueChange = onPasswordChange,
           visualTransformation = PasswordVisualTransformation(),
           leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "Password") },
           keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Go))

       TextButton(onClick = {onForgotPasswordClick },
           modifier = Modifier.align(Alignment.End)) {
           Text(text = "Forgot Password?",
               textAlign = TextAlign.End)
       }
   }

}

@Composable
fun LoginFooter(
    onSignInClick:() -> Unit,
    onSignUpClick:() -> Unit
) {
Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Button(onClick = { onSignInClick }, modifier = Modifier.fillMaxWidth()) {
        Text(text = "Sign In")
    }
    TextButton(onClick = { onSignUpClick }) {
        Text(text = "Don't have an account, Click Here")

    }
}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DemoField(
    value: String,
    label: String,
    placeholder: String,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        label = {
            Text(text = label)
        },
        placeholder = {
            Text(text = placeholder)
        },
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        onValueChange = onValueChange
    )
}

private fun Context.showToast(msg:String){
    Toast.makeText(this,msg,Toast.LENGTH_SHORT).show()
}