package com.nikhilchauhan.contextual_cards.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nikhilchauhan.contextual_cards.data.remote.responsemodel.CardsResponse.CardGroup.Card
import com.nikhilchauhan.contextual_cards.databinding.FragmentHC5Binding
import com.nikhilchauhan.contextual_cards.ui.CardGroupTypes.Hc3
import com.nikhilchauhan.contextual_cards.ui.CardsVM
import com.nikhilchauhan.contextual_cards.ui.adapters.Hc3Adapter
import kotlinx.coroutines.launch

class HC5Fragment : Fragment() {
  private var _binding: FragmentHC5Binding? = null
  private val binding
    get() = _binding

  private lateinit var hc3Adapter: Hc3Adapter

  private val cardsVM: CardsVM by activityViewModels()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    _binding = FragmentHC5Binding.inflate(layoutInflater, container, false)

    binding?.apply {
      viewLifecycleOwner.lifecycleScope.launch {
        cardsVM.cardsHashMap.collect { hashMap ->
          hc3Adapter = Hc3Adapter(hashMap[Hc3] ?: emptyList<Card>())
          rvHc5.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = hc3Adapter
          }

        }
      }
    }

    return binding?.root
  }
}