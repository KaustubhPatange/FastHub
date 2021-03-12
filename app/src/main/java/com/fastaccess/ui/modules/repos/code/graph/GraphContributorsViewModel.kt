package com.fastaccess.ui.modules.repos.code.graph

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.fastaccess.data.service.RepoService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class GraphContributorsViewModel(
        private val repoService: RepoService,
        private val owner: String,
        private val repo: String
) : ViewModel() {
    val contributions = MutableStateFlow<StatsModel?>(null)
    val error = MutableStateFlow<String?>(null)
    init {
        viewModelScope.launch {
            try {
                val model = repoService.getContributors(owner, repo)
                val response = model.string()
                model.close() // Close when not needed
                val statsModel : StatsModel? = Gson().fromJson(response, object : TypeToken<StatsModel?>() {}.type)
                if (statsModel != null) {
                    contributions.value = statsModel
                }
            }catch (e: Exception) {
                error.value = e.message
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
class GraphContributorsViewModelFactory(
        private val provider: RepoService,
        private val owner: String,
        private val repo: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(GraphContributorsViewModel::class.java)) {
            GraphContributorsViewModel(provider, owner, repo) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}