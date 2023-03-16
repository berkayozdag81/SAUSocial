package com.berkayozdag.sausocial.common


data class Post(
    var id: String,
    var userImage: String,
    var userDepartment: String,
    var userName: String,
    var createdDate: String,
    var description: String,
    var numberOfLike: Int,
    var numberOfComment: Int,
    var postImage: String?,
)

object HomeMockData {
    private val posts = arrayListOf(
        Post(
            "1",
            "userImage",
            "Computer Engineer",
            "Summers Fin",
            "15.03.2023 13:50",
            "We are going to be here again next week. You should come eaerlier so you can get in the front.",
            15,
            45,
            null
        ),
        Post(
            "2",
            "userImage",
            "Chemical engineering",
            "Kurt Wagner",
            "15.03.2023 13:40",
            "I hope students reading this realize that OOS public flagships, like Michigan, especially for engineering, shouldn't be",
            58,
            58,
            null
        ),
        Post(
            "3",
            "userImage",
            "Electrical engineering",
            "Kevin Sydney",
            "15.03.2023 13:12",
            "UCLA professor named one of 2019’s 100 most influential Africans" + "\uD83D\uDE0D\uD83D\uDC4D\uD83C\uDFFC",
            63546,
            545,
            "https://media.sproutsocial.com/uploads/2022/04/Best-times-to-post-2022_BTTP-Social-Media.jpg"
        ),
        Post(
            "4",
            "userImage",
            "Computer Engineer",
            "Ahmet Selim Öztürk",
            "15.03.2023 17:01",
            "First decade began from year 0001. So the new decade will be next year, 2021. But it's okay to make mistake for very first time",
            15,
            4,
            null
        ),
        Post(
            "5",
            "userImage",
            "Computer Engineer",
            "Berkay Özdağ",
            "15.03.2023 17:05",
            "What do you think about Prof. Edmuns new policy?",
            10,
            5,
            "https://media.sproutsocial.com/uploads/2022/10/social-media-post-ideas-for-your-brand-in-2022.svg"
        ),
        Post(
            "6",
            "userImage",
            "Electrical engineering",
            "Scott McKay",
            "15.03.2023 17:01",
            "Which one is your favorite from current Democratic nominations?",
            32,
            34,
            null
        ),
    )

    fun getPost(): ArrayList<Post> {
        return posts
    }
}