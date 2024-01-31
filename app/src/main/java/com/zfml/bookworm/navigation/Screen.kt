package com.zfml.bookworm.navigation

import com.zfml.bookworm.core.Constants.ADD_BOOK_SCREEN
import com.zfml.bookworm.core.Constants.ADD_BOOK_SCREEN_ARG_KEY
import com.zfml.bookworm.core.Constants.HOME_SCREEN
import com.zfml.bookworm.core.Constants.SIGN_IN_SCREEN
import com.zfml.bookworm.core.Constants.SIGN_UP_SCREEN

sealed class Screen (val route: String){
   object SignInScreen: Screen(SIGN_IN_SCREEN)
   object SignUpScreen: Screen(SIGN_UP_SCREEN)
   object HomeScreen: Screen(HOME_SCREEN)
   object AddScreen: Screen("$ADD_BOOK_SCREEN?$ADD_BOOK_SCREEN_ARG_KEY={$ADD_BOOK_SCREEN_ARG_KEY}") {
      fun passBookId(bookId: String) =
         "$ADD_BOOK_SCREEN?$ADD_BOOK_SCREEN_ARG_KEY= $bookId"
   }
}