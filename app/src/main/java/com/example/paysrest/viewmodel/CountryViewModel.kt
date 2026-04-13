package com.example.paysrest.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paysrest.data.CountryApi
import com.example.paysrest.model.Country
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class CountryFilter {
    ALL, AFRICA
}

class CountryViewModel : ViewModel() {
    private val api = CountryApi.create()

    private val _allCountries = MutableStateFlow<List<Country>>(emptyList())
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    // Filtrage local pour la recherche
    val filteredCountries: StateFlow<List<Country>> = combine(_allCountries, _searchQuery) { countries, query ->
        if (query.isBlank()) {
            countries
        } else {
            countries.filter { 
                it.name.common.contains(query, ignoreCase = true) 
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun fetchCountries(filter: CountryFilter) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            _searchQuery.value = "" // Reset search on new fetch
            try {
                val result = when (filter) {
                    CountryFilter.ALL -> api.getAllCountries()
                    CountryFilter.AFRICA -> api.getCountriesByRegion("africa")
                }
                Log.d("CountryViewModel", "Fetched ${result.size} countries")
                _allCountries.value = result
            } catch (e: Exception) {
                Log.e("CountryViewModel", "Error fetching countries", e)
                _errorMessage.value = e.localizedMessage ?: "Une erreur est survenue"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
    }
}
