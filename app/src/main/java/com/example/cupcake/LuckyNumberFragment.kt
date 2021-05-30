package com.example.cupcake

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.cupcake.databinding.FragmentLuckyNumberBinding
import com.example.cupcake.games.GameFunctions
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class LuckyNumberFragment: Fragment() {
    private var rollCount = 1

    //Data binding with this class and UI
    private var binding: FragmentLuckyNumberBinding? = null

    //"activityViewModels()" property delegation to call "GameFunctions" methods
    private val gameView: GameFunctions by activityViewModels()

    //Binding for int only
    private lateinit var numberBinding: FragmentLuckyNumberBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentLuckyNumberBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            //Sets software life cycle owner
            lifecycleOwner = viewLifecycleOwner

            //Bind view model with shared model inside layout
            viewModel = gameView

            //Binds data variable with fragment instance (XML data variable calls functions)
            luckyNumberFragment = this@LuckyNumberFragment
        }
    }

    //Play Roll Again game
    fun playGame() {
        //New die
        val dice = DiceA(6)
        val dieRoll = dice.roll()

        //Die image, show image, and show roll result
        val diceImage: ImageView = requireView().findViewById(R.id.imageView)
        diceImage.setVisibility(View.VISIBLE)
        dice.rollResult(diceImage, dieRoll)

        if (rollCount == 1) {
            //Set roll and display message
            gameView.setRoll1(dieRoll)
            setMessageLucky(getString(R.string.first_roll), getString(R.string.lucky_roll,
                gameView.num.value), getString(R.string.ok))
        }
        if (rollCount == 2) {
            //Win game or not (result and message)?
            val result = gameView.rollAgain(dieRoll)

            //Show result of game
            gameView.gamePlayResult(result)
            wonGame(result)
        }
    }

    private fun wonGame(result: Boolean) {
        //If won game
        if (result == true) {
            //Display message
            setMessage(getString(R.string.congratulations), getString(R.string.you_won),
                getString(R.string.exit), getString(R.string.play_again))
        }
        //If lost game
        else {
            //Display message
            setMessage(getString(R.string.sorry), getString(R.string.you_lost),
                getString(R.string.exit), getString(R.string.play_again))
        }
    }

    //Message in pop up
    private fun setMessage(title: String, message: String, neg: String, pos: String) {
        //Display message
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            //Shows the value of mutable live data of score (modified)
            .setMessage(message)

            .setCancelable(false)
            .setPositiveButton(neg) { _, _ ->
                endGame()
            }
            .setNegativeButton(pos) { _, _ ->
                //Go back to roll 1
                --rollCount
                playAgain()
            }
            .show()
    }

    //Message for lucky number roll
    private fun setMessageLucky(title: String, message: String, pos: String) {
        //Display message
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            //Shows the value of mutable live data of score (modified)
            .setMessage(message)

            .setCancelable(false)
            .setPositiveButton(pos) { _, _ ->
                //Go to roll 2
                ++rollCount
                playAgain()
            }
            .show()
    }

    //Go to summary of game played
    fun endGame() {
        findNavController().navigate(R.id.action_luckyNumberFragment_to_resultsFragment)
    }

    //Destroy values and reset data binding
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    //Cancel by resetting the game view and navigating back to start
    fun quit() {
        gameView.resetValues()
        findNavController().navigate(R.id.action_luckyNumberFragment_to_startFragment)
    }

    //Play game again if user wants to
    private fun playAgain() {
        //Reset values, stick to same game
        if (rollCount == 1)
            gameView.resetValues()
        gameView.playGame("Lucky Number")

        //Image of die (hide image
        val diceImage: ImageView = requireView().findViewById(R.id.imageView)
        diceImage.setVisibility(View.INVISIBLE)
    }
}

//Roll dice in its own class
class DiceA(val numSides: Int){
    //Roll dice logic
    fun roll(): Int {
        return (1..numSides).random()
    }

    fun rollResult(diceImage: ImageView, die: Int) {
        //Result of rolling die
        val drawable = when (die) {
            0 -> null
            1 -> R.drawable.dice_1
            2 -> R.drawable.dice_2
            3 -> R.drawable.dice_3
            4 -> R.drawable.dice_4
            5 -> R.drawable.dice_5
            else -> R.drawable.dice_6
        }
        //Display visual output
        if (drawable != null)
            diceImage.setImageResource(drawable)

        //Delay for user friendliness
        Thread.sleep(200)
    }
}