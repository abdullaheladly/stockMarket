package com.abdullah996.stockmarketapp.presentation.company_listings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.abdullah996.stockmarketapp.domain.repository.StockRepository
import com.abdullah996.stockmarketapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CompanyListingsViewModel @Inject constructor(
    private val repository: StockRepository
) :ViewModel() {
    var state by mutableStateOf(CompanyListingsState())

    private var searchJob:Job?=null

    init {
        getCompanyListings()
    }

    fun onEvent(event: CompanyListingEvent){
        when(event){
            is CompanyListingEvent.Refresh->{
                    getCompanyListings(fetchFromRemote = true)
            }
            is CompanyListingEvent.OnSearchQueryChanged->{
                state=state.copy(searchQuery = event.query)
                searchJob?.cancel()
                searchJob=viewModelScope.launch {
                    delay(500)
                     getCompanyListings()
                }
            }
        }
    }

    private fun getCompanyListings(
        query:String=state.searchQuery.lowercase(),
        fetchFromRemote:Boolean=false
    ){
      viewModelScope.launch {
          repository.getCompanyListings(fetchFromRemote, query)
              .collect{result->
                  when(result){
                      is Resource.Success->{
                        result.data?.let {listings->
                            state=state.copy(
                                companies = listings
                            )

                        }
                      }
                      is Resource.Error->Unit
                      is Resource.Loading->{
                          state=state.copy(isLoading = result.isLoading)
                      }
                  }

              }
      }
    }
}