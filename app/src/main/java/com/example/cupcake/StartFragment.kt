package com.example.cupcake

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.cupcake.databinding.FragmentStartBinding
import com.example.cupcake.games.GameFunctions

class StartFragment: Fragment() {
    //Data binding with this class and UI
    private var binding: FragmentStartBinding? = null

    //"activityViewModels()" property delegation to call "GameFunctions" methods
    private val gameView: GameFunctions by activityViewModels()

    //Data bind string name constraint
    private lateinit var bindingString: StartFragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentStartBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Bind data with start fragment outlook and its declared values (modified)
        binding?.startFragment = this
    }

    fun chooseGame(game: String) {
        //Set the game as "game" parameter
        gameView.playGame(game)

        //Based on button selected, navigate to relevant game
        val gamePlay = gameView.getPlayGame()
        if (gamePlay == "Lucky Number")
            findNavController().navigate(R.id.action_startFragment_to_luckyNumberFragment)
        else if (gamePlay == "Double or Nothing")
            findNavController().navigate(R.id.action_startFragment_to_doubleOrNothingFragment)
        else
            findNavController().navigate(R.id.action_startFragment_to_yahtzeeFragment)
    }

    //Destroy values and reset data binding
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    //Exit the game when button pressed
    fun exitGame() {
        activity?.finish()
    }
}