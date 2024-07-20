package com.applogin.bdsqlite

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.applogin.bdsqlite.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ArrayAdapter<User>
    private var pos: Int = -1

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val db = DBHelper(this)

        val listUsers = db.userListSelectAll()

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listUsers)
        binding.listView.adapter = adapter

        binding.listView.setOnItemClickListener { _, _, position, _ ->
            binding.textId.setText("ID: ${listUsers[position].id}")
            binding.editUsername.setText(listUsers[position].username)
            binding.editPassword.setText(listUsers[position].password)
            pos = position
        }

        binding.buttonInsert.setOnClickListener {
            val username = binding.editUsername.text.toString()
            val password = binding.editPassword.text.toString()

            val res = db.userInsert(username, password)

            if (res > 0) {
                Toast.makeText(this, "Usuário inserido com sucesso: $res", Toast.LENGTH_SHORT)
                    .show()
                listUsers.add(User(res.toInt(), username, password))
                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(this, "Erro ao inserir usuário: $res", Toast.LENGTH_SHORT).show()
            }
        }
        binding.buttonUpdate.setOnClickListener {
            if (pos >= 0) {
                val id = listUsers[pos].id
                val username = binding.editUsername.text.toString()
                val password = binding.editPassword.text.toString()

                val res = db.userUpdate(id, username, password)

                if (res > 0) {
                    Toast.makeText(this, "Usuário atualizado com sucesso: $res", Toast.LENGTH_SHORT)
                        .show()
                    listUsers[pos].username = username
                    listUsers[pos].password = password
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this, "Erro ao atualizar usuário: $res", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
        binding.buttonDelete.setOnClickListener {
            if (pos >= 0) {
                val id = listUsers[pos].id
                var res = db.userDelete(id)
                if (res > 0) {
                    Toast.makeText(this, "Usuário excluido com sucesso: $res", Toast.LENGTH_SHORT)
                        .show()
                    listUsers.removeAt(pos)
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this, "Erro ao excluir usuário: $res", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

    }
}