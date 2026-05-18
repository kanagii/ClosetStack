package com.example.closetstack

data class UserProfile(
    var displayName: String = "Vanessa Kung",
    var username: String = "vanessa_kung",
    var location: String = "Philippines",
    var bio: String = "Fashion and Styling Enthusiast",
    var interests: String = "#Core  #FreeNovaJeans  #ForTheStreets\n#StreetWear  #hiphop",
    var avatarRes: Int = R.drawable.usertop1
)