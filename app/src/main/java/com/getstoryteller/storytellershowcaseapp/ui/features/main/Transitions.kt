package com.getstoryteller.storytellershowcaseapp.ui.features.main

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Left
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Right
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry
import com.getstoryteller.storytellershowcaseapp.ui.utils.comingFrom
import com.getstoryteller.storytellershowcaseapp.ui.utils.goingTo

object Transitions {

  fun homeScreenExitTransition(): AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition? =
    {
      if (goingTo("home/moments")) {
        ExitTransition.None
      } else {
        slideOutOfContainer(
          towards = Left,
          animationSpec = tween(700),
        )
      }
    }

  fun homeScreenEnterTransition(): AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition? =
    {
      if (comingFrom("home/moments")) {
        EnterTransition.None
      } else {
        slideIntoContainer(
          towards = Right,
          animationSpec = tween(700),
        )
      }
    }

  fun momentsScreenExitTransition(): AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition? =
    {
      if (comingFrom("home/link")) {
        slideOutOfContainer(
          towards = Left,
          animationSpec = tween(700),
        )
      } else {
        ExitTransition.None
      }
    }

  fun momentsScreenEnterTransition(): AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition? =
    {
      if (goingTo("home/link")) {
        slideIntoContainer(
          towards = Right,
          animationSpec = tween(700),
        )
      } else {
        EnterTransition.None
      }
    }

  fun moreScreenExitTransition(): AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition? =
    {
      if (goingTo("home/link")) {
        slideOutOfContainer(
          towards = Left,
          animationSpec = tween(700),
        )
      } else {
        slideOutOfContainer(
          towards = Right,
          animationSpec = tween(700),
        )
      }
    }

  fun moreScreenEnterTransition(): AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition? =
    {
      if (comingFrom("home/link")) {
        slideIntoContainer(
          towards = Right,
          animationSpec = tween(700),
        )
      } else {
        slideIntoContainer(
          towards = Left,
          animationSpec = tween(700),
        )
      }
    }

  fun linkScreenEnterTransition(): AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition? =
    {
      slideIntoContainer(
        towards = Left,
        animationSpec = tween(500),
      )
    }

  fun linkScreenExitTransition(): AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition? =
    {
      slideOutOfContainer(
        towards = Right,
        animationSpec = tween(700),
      )
    }

  fun accountScreenExitTransition(): AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition? =
    {
      if (goingTo("account")) {
        slideOutOfContainer(
          towards = Left,
          animationSpec = tween(700),
        )
      } else {
        slideOutOfContainer(
          towards = Right,
          animationSpec = tween(700),
        )
      }
    }

  fun accountScreenEnterTransition(): AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition? =
    {
      if (comingFrom("account")) {
        slideIntoContainer(
          towards = Right,
          animationSpec = tween(700),
        )
      } else {
        slideIntoContainer(
          towards = Left,
          animationSpec = tween(700),
        )
      }
    }

  fun accountOptionsScreenExitTransition(): AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition? =
    { slideOutOfContainer(towards = Right, animationSpec = tween(700)) }

  fun accountOptionsScreenEnterTransition(): AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition? =
    { slideIntoContainer(towards = Left, animationSpec = tween(700)) }
}
