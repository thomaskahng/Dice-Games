package com.example.cupcake

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.cupcake.databinding.FragmentYahtzeeBinding
import com.example.cupcake.games.GameFunctions
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class YahtzeeFragment: Fragment() {
    //Data binding with this class and UI
    private var binding: FragmentYahtzeeBinding? = null

    //"activityViewModels()" property delegation to call "GameFunctions" methods
    private val gameView: GameFunctions by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentYahtzeeBinding.inflate(inflater, container, false)
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
            yahtzeeFragment = this@YahtzeeFragment
        }
    }

    //Play the game
    fun playGame() {
        //New dice
        val dice = DiceC(6)

        //Roll the dice
        val dice1 = dice.roll()
        val dice2 = dice.roll()
        val dice3 = dice.roll()
        val dice4 = dice.roll()
        val dice5 = dice.roll()

        //Images of dice
        val diceImage1: ImageView = requireView().findViewById(R.id.imageView)
        val diceImage2: ImageView = requireView().findViewById(R.id.imageView2)
        val diceImage3: ImageView = requireView().findViewById(R.id.imageView3)
        val diceImage4: ImageView = requireView().findViewById(R.id.imageView4)
        val diceImage5: ImageView = requireView().findViewById(R.id.imageView5)

        //Show images
        visible(diceImage1, diceImage2, diceImage3, diceImage4, diceImage5, 0)

        //Dice rolls
        dice.rollResult(diceImage1, dice1)
        dice.rollResult(diceImage2, dice2)
        dice.rollResult(diceImage3, dice3)
        dice.rollResult(diceImage4, dice4)
        dice.rollResult(diceImage5, dice5)

        //Game result
        var res = gameView.yahtzee(dice1, dice2, dice3, dice4, dice5)

        //Won game?
        if (res != null) {
            wonGame(res)
            gameView.gamePlayResult(res)
        }
    }

    //See if user won the game
    private fun wonGame(winOrLose: Boolean) {
        //If won game
        if (winOrLose == true) {
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
            .setPositiveButton(pos) { _, _ ->
                playAgain()
            }
            .setNegativeButton(neg) { _, _ ->
                endGame()
            }
            .show()
    }

    //Go to summary of game played
    fun endGame() {
        findNavController().navigate(R.id.action_yahtzeeFragment_to_resultsFragment)
    }

    //Destroy values and reset data binding
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    //Cancel by resetting the game view and navigating back to start
    fun quit() {
        gameView.resetValues()
        findNavController().navigate(R.id.action_yahtzeeFragment_to_startFragment)
    }

    fun playAgain() {
        //Reset values and stick to same game
        gameView.resetValues()
        gameView.playGame("Yahtzee")

        //Images of dice
        val diceImage1: ImageView = requireView().findViewById(R.id.imageView)
        val diceImage2: ImageView = requireView().findViewById(R.id.imageView2)
        val diceImage3: ImageView = requireView().findViewById(R.id.imageView3)
        val diceImage4: ImageView = requireView().findViewById(R.id.imageView4)
        val diceImage5: ImageView = requireView().findViewById(R.id.imageView5)

        //Hide images
        visible(diceImage1, diceImage2, diceImage3, diceImage4, diceImage5, 1)
    }
}

private fun visible(image1: ImageView, image2: ImageView, image3: ImageView,
image4: ImageView, image5: ImageView, flag: Int) {
    if (flag == 0)  {
        image1.setVisibility(View.VISIBLE)
        image2.setVisibility(View.VISIBLE)
        image3.setVisibility(View.VISIBLE)
        image4.setVisibility(View.VISIBLE)
        image5.setVisibility(View.VISIBLE)
    }
    //Hide images
    else {
        image1.setVisibility(View.INVISIBLE)
        image2.setVisibility(View.INVISIBLE)
        image3.setVisibility(View.INVISIBLE)
        image4.setVisibility(View.INVISIBLE)
        image5.setVisibility(View.INVISIBLE)
    }
}

//Roll dice in its own class
class DiceC(val numSides: Int){
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