package com.example.testapp

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testapp.ui.theme.TestAppTheme
import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.google.firebase.firestore.firestore

val firestore = Firebase.firestore


class Usuario {
    var nome = ""
    var endereco = ""
    var bairro = ""
    var cep = ""
    var cidade = ""
    var estado = ""
}

// Add a new document with a generated ID
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppPreview()
        }
    }
}



@Composable
fun App() {
    val usuarios = remember { mutableStateListOf<Usuario>() }

    val usuariosRef = firestore.collection("users")

    usuariosRef.addSnapshotListener { snapshots, e ->
        if (e != null) {
            Log.w(TAG, "Listen failed.", e)
            return@addSnapshotListener
        }

        if (snapshots != null && !snapshots.isEmpty) {
            usuarios.clear() // Limpa a lista antes de atualizar com novos dados

            for (document in snapshots) {
                val usuario = Usuario().apply {
                    nome = document.getString("nome") ?: ""
                    endereco = document.getString("endereco") ?: ""
                    bairro = document.getString("bairro") ?: ""
                    cep = document.getString("cep") ?: ""
                    cidade = document.getString("cidade") ?: ""
                    estado = document.getString("estado") ?: ""
                }
                usuarios.add(usuario)
            }

            Log.d(TAG, "Usuários atualizados: ${usuarios.size}")
        } else {
            Log.d(TAG, "Nenhum dado encontrado.")
            usuarios.clear()
        }
    }


    var nome by remember{
        mutableStateOf("")
    }
    var endereco by remember{
        mutableStateOf("")
    }
    var bairro by remember{
        mutableStateOf("")
    }
    var cep by remember{
        mutableStateOf("")
    }
    var cidade by remember{
        mutableStateOf("")
    }
    var estado by remember{
        mutableStateOf("")
    }
    val user = hashMapOf(
        "nome" to nome,
        "endereco" to endereco,
        "bairro" to bairro,
        "cep" to cep,
        "cidade" to cidade,
        "estado" to estado,
    )

    Column(
        Modifier.fillMaxSize(),

        ) {
        Row(
            modifier =
            Modifier.fillMaxWidth()
                .padding(0.dp, 34.dp),
            Arrangement.Center,

            ) {
            Text(
                text = "App Agendamento",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            Arrangement.Center
        ) {
            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome") }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            Arrangement.Center
        ) {
            OutlinedTextField(
                value = endereco,
                onValueChange = { endereco = it },
                label = { Text("Endereco") }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            Arrangement.Center
        ) {
            OutlinedTextField(
                value = bairro,
                onValueChange = { bairro = it },
                label = { Text("Bairro") }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            Arrangement.Center
        ) {
            OutlinedTextField(
                value = cep,
                onValueChange = { cep = it },
                label = { Text("CEP") }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            Arrangement.Center
        ) {
            OutlinedTextField(
                value = cidade,
                onValueChange = { cidade = it },
                label = { Text("Cidade") }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            Arrangement.Center
        ) {
            OutlinedTextField(
                value = estado,
                onValueChange = { estado = it },
                label = { Text("Estado") }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            Arrangement.Center
        ) {
            Column(Modifier.fillMaxWidth(0.35f)) {
                Button(
                    onClick = {
                        firestore.collection("users")
                            .add(user)
                            .addOnSuccessListener { documentReference ->
                                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                            }
                            .addOnFailureListener { e ->
                                Log.w(TAG, "Error adding document", e)
                            }
                    },
                ) {
                    Text(
                        text = "Cadastrar",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        val scrollState = rememberScrollState()

        Column {
            Row(
                modifier = Modifier
                    .horizontalScroll(scrollState)
            ) {
                // Cabeçalhos das colunas
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(text = "Nome", style = MaterialTheme.typography.labelMedium)
                    usuarios.forEach {
                        Text(text = it.nome)
                    }
                }

                Column(modifier = Modifier.padding(8.dp)) {
                    Text(text = "Endereço", style = MaterialTheme.typography.labelMedium)
                    usuarios.forEach {
                        Text(text = it.endereco)
                    }
                }

                Column(modifier = Modifier.padding(8.dp)) {
                    Text(text = "Bairro", style = MaterialTheme.typography.labelMedium)
                    usuarios.forEach {
                        Text(text = it.bairro)
                    }
                }

                Column(modifier = Modifier.padding(8.dp)) {
                    Text(text = "CEP", style = MaterialTheme.typography.labelMedium)
                    usuarios.forEach {
                        Text(text = it.cep)
                    }
                }

                Column(modifier = Modifier.padding(8.dp)) {
                    Text(text = "Cidade", style = MaterialTheme.typography.labelMedium)
                    usuarios.forEach {
                        Text(text = it.cidade)
                    }
                }

                Column(modifier = Modifier.padding(8.dp)) {
                    Text(text = "Estado", style = MaterialTheme.typography.labelMedium)
                    usuarios.forEach {
                        Text(text = it.estado)
                    }
                }
            }
        }

    }
}


    @Preview(showBackground = true)
    @Composable
    fun AppPreview() {
        TestAppTheme {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                App()
            }
        }
    }