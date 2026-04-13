package com.example.paysrest.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.paysrest.R
import com.example.paysrest.model.Country
import com.example.paysrest.viewmodel.CountryFilter
import com.example.paysrest.viewmodel.CountryViewModel

@Composable
fun MainNavigation(viewModel: CountryViewModel = viewModel()) {
    var currentScreen by remember { mutableStateOf("home") }

    when (currentScreen) {
        "home" -> HomeScreen(
            onOptionSelected = { filter ->
                viewModel.fetchCountries(filter)
                currentScreen = "list"
            }
        )
        "list" -> CountryApp(
            viewModel = viewModel,
            onBack = { currentScreen = "home" }
        )
    }
}

@Composable
fun HomeScreen(onOptionSelected: (CountryFilter) -> Unit) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(150.dp)
                    .padding(bottom = 16.dp),
                contentScale = ContentScale.Fit
            )
            
            Text(
                text = "Bienvenue sur PaysRest",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 32.dp)
            )
            Button(
                onClick = { onOptionSelected(CountryFilter.ALL) },
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("Tous les pays du monde")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { onOptionSelected(CountryFilter.AFRICA) },
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("Pays d'Afrique")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryApp(viewModel: CountryViewModel, onBack: () -> Unit) {
    val countries by viewModel.filteredCountries.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Liste des Pays") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text("←") 
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            SearchBar(query = searchQuery, onQueryChanged = { viewModel.onSearchQueryChanged(it) })

            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                if (isLoading && countries.isEmpty()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else if (errorMessage != null && countries.isEmpty()) {
                    Text(text = errorMessage!!, color = Color.Red, modifier = Modifier.align(Alignment.Center))
                } else {
                    LazyColumn {
                        items(countries) { country ->
                            CountryItem(country)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBar(query: String, onQueryChanged: (String) -> Unit) {
    TextField(
        value = query,
        onValueChange = onQueryChanged,
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        placeholder = { Text("Rechercher un pays...") },
        singleLine = true
    )
}

@Composable
fun CountryItem(country: Country) {
    Card(
        modifier = Modifier.padding(8.dp).fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(country.flags.png)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                placeholder = painterResource(R.drawable.load),
                error = painterResource(R.drawable.error),
                modifier = Modifier.width(100.dp).height(60.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = country.translations.fra.common, style = MaterialTheme.typography.titleMedium)
                Text(text = "Capitale: ${country.capital?.joinToString() ?: "N/A"}", style = MaterialTheme.typography.bodySmall)
                Text(text = "Continent: ${country.continents.joinToString()}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
