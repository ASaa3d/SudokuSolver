package com.example.sudokusolver.auth.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

@Composable
fun LoginScreen(
    navBackStack: NavBackStack<NavKey>,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {},

        modifier= modifier.fillMaxSize()
    ) { paddingValues ->
        LoginComponent(modifier = Modifier.padding(paddingValues))
    }
}