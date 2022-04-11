package com.abdullah996.stockmarketapp.presentation.company_listings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.ActivityNavigator
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination(start = true)
fun CompanyListingsScreen(
    navigator: DestinationsNavigator,
    viewModel: CompanyListingsViewModel= hiltViewModel()
){
    val swipeRefreshState= rememberSwipeRefreshState(isRefreshing = viewModel.state.isRefreshing)
    val state=viewModel.state
    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value =state.searchQuery ,
            onValueChange ={
                viewModel.onEvent(CompanyListingEvent.OnSearchQueryChanged(it)
                )
            },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            placeholder = {Text(text = "search")},
            maxLines = 1,
            singleLine = true
        )
        SwipeRefresh(
            state =swipeRefreshState ,
            onRefresh = { viewModel.onEvent(CompanyListingEvent.Refresh) }
        ) {
           LazyColumn(modifier = Modifier.fillMaxSize()){
               items(state.companies.size){i->
                   val company=state.companies[i]
                   CompanyItem(company =company,
                   modifier = Modifier.fillMaxWidth()
                       .clickable {
                           //TODO:Navigate
                       }.padding(
                           16.dp
                       ))
                   if (i<state.companies.size){
                       Divider(modifier = Modifier.padding(
                           horizontal = 16.dp,

                       ))
                   }

               }
           }
        }
    }

}