package com.berkayozdag.sausocial.common


data class Post(
    var id: String,
    var user: User,
    var createdDate: String,
    var description: String,
    var numberOfLike: Int,
    var numberOfComment: Int,
    var postImage: String?,
    var comments: List<Comment>?
)

data class Comment(
    var id: String,
    var comment: String,
    var user: User,
)

data class User(
    var id: String,
    var userImage: String,
    var userDepartment: String,
    var userName: String,
)

data class Group(
    var id: String,
    var name: String,
    var users: List<User>,
    var posts: List<Post>,
    var imageUrl: String
)

object HomeMockData {
    private val users = arrayListOf(
        User(
            "1",
            "userImage",
            "Computer Engineer",
            "Berkay Özdağ"
        ),
        User(
            "1",
            "userImage",
            "Computer Engineer",
            "Selim Öztürk"
        ),
        User(
            "1",
            "userImage",
            "Computer Engineer",
            "Adem Kepçe"
        ),
        User(
            "1",
            "userImage",
            "Computer Engineer",
            "Burak Can Özdağ"
        ),
        User(
            "1",
            "userImage",
            "Computer Engineer",
            "Furkan Ergün"
        ),
        User(
            "1",
            "userImage",
            "Computer Engineer",
            "Davud Samed Tombul"
        ),
        User(
            "1",
            "userImage",
            "Computer Engineer",
            "Furkan Kablan"
        )

    )

    private val posts = arrayListOf(
        Post(
            "1",
            User("1", "userImage", "Computer Engineer", "Summers Fin"),
            "15.03.2023 13:50",
            "We are going to be here again next week. You should come eaerlier so you can get in the front.",
            15,
            45,
            null,
            arrayListOf(
                Comment(
                    "1",
                    "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's",
                    User("1", "userImage", "Computer Engineer", "Berkay Özdağ")
                ),
                Comment(
                    "2",
                    "Temporibus autem quibusdam et aut officiis debitis aut rerum necessitatibus saepe eveniet ut et voluptates repudiandae sint et molestiae non recusandae.",
                    User("1", "userImage", "Computer Engineer", "Berkay Özdağ")
                ),
                Comment(
                    "3",
                    "Itaque earum rerum hic tenetur a sapiente delectus, ut aut reiciendis voluptatibus maiores alias consequatur aut perferendis doloribus asperiores repellat.",
                    User("1", "userImage", "Computer Engineer", "Berkay Özdağ")
                ),
                Comment(
                    "4",
                    "Quod maxime placeat facere possimus",
                    User("1", "userImage", "Computer Engineer", "Berkay Özdağ")
                ),
                Comment(
                    "5",
                    "Temporibus autem quibusdam et aut officiis debitis aut rerum necessitatibus saepe eveniet ut et voluptates repudiandae sint et molestiae non recusandae.",
                    User("1", "userImage", "Computer Engineer", "Berkay Özdağ")
                ),
                Comment(
                    "6",
                    "vel illum qui dolorem eum fugiat.",
                    User("1", "userImage", "Computer Engineer", "Berkay Özdağ")
                ),
            )
        ),
        Post(
            "2",
            User("2", "userImage", "Chemical engineering", "Kurt Wagner"),
            "15.03.2023 13:40",
            "I hope students reading this realize that OOS public flagships, like Michigan, especially for engineering, shouldn't be",
            58,
            58,
            null,
            null
        ),
        Post(
            "3",
            User("3", "userImage", "Electrical engineering", "Kevin Sydney"),
            "15.03.2023 13:12",
            "UCLA professor named one of 2019’s 100 most influential Africans" + "\uD83D\uDE0D\uD83D\uDC4D\uD83C\uDFFC",
            63546,
            545,
            "https://media.sproutsocial.com/uploads/2022/04/Best-times-to-post-2022_BTTP-Social-Media.jpg",
            null
        ),
        Post(
            "4",
            User("4", "userImage", "Computer Engineer", "Ahmet Selim Öztürk"),
            "15.03.2023 17:01",
            "First decade began from year 0001. So the new decade will be next year, 2021. But it's okay to make mistake for very first time",
            15,
            4,
            null,
            null
        ),
        Post(
            "5",
            User("5", "userImage", "Computer Engineer", "Berkay Özdağ"),
            "15.03.2023 17:05",
            "What do you think about Prof. Edmuns new policy?",
            10,
            5,
            "https://media.sproutsocial.com/uploads/2022/10/social-media-post-ideas-for-your-brand-in-2022.svg",
            null
        ),
        Post(
            "6",
            User("6", "userImage", "Electrical engineering", "Scott McKay"),
            "15.03.2023 17:01",
            "Which one is your favorite from current Democratic nominations?",
            32,
            34,
            null,
            null
        ),
    )

    private val groups = arrayListOf(
        Group(
            "1",
            "Bilgisayar Topluluğu",
            arrayListOf(
                User("2", "userImage", "Chemical engineering", "Kurt Wagner"),
                User("6", "userImage", "Electrical engineering", "Scott McKay"),
                User("5", "userImage", "Computer Engineer", "Berkay Özdağ")
            ),
            posts,
            "https://instagram.fist4-1.fna.fbcdn.net/v/t51.2885-19/244472456_3257067711085233_281870210449471368_n.jpg?stp=dst-jpg_s320x320&_nc_ht=instagram.fist4-1.fna.fbcdn.net&_nc_cat=109&_nc_ohc=uU-7aJbVmCoAX89E9Ws&edm=AOQ1c0wBAAAA&ccb=7-5&oh=00_AfATBnSqt-JUZKy8rFptc0EGCQ5d14_rl43g3_3xBYcT-w&oe=6421D40B&_nc_sid=8fd12b"
        ),
        Group(
            "1",
            "Genç Girişimciler Kulübü",
            arrayListOf(
                User("2", "userImage", "Chemical engineering", "Kurt Wagner"),
                User("6", "userImage", "Electrical engineering", "Scott McKay"),
                User("5", "userImage", "Computer Engineer", "Berkay Özdağ")
            ),
            posts,
            "https://instagram.fist4-1.fna.fbcdn.net/v/t51.2885-19/281952301_469355788294468_4019386830399695627_n.jpg?stp=dst-jpg_s320x320&_nc_ht=instagram.fist4-1.fna.fbcdn.net&_nc_cat=104&_nc_ohc=gvFVUIVjORgAX-8I86f&edm=AOQ1c0wBAAAA&ccb=7-5&oh=00_AfBOZ1usDXaJVxZ_0q1A_aHNqoe7C-G2Zz3nXObQxpAzBw&oe=6421826F&_nc_sid=8fd12b"
        ),
        Group(
            "1",
            "Endüstri Mühendisliği Kulübü ",
            arrayListOf(
                User("2", "userImage", "Chemical engineering", "Kurt Wagner"),
                User("6", "userImage", "Electrical engineering", "Scott McKay"),
                User("5", "userImage", "Computer Engineer", "Berkay Özdağ")
            ),
            posts,
            "https://instagram.fist4-1.fna.fbcdn.net/v/t51.2885-19/69834749_2941742332519146_3893734565058969600_n.jpg?stp=dst-jpg_s320x320&_nc_ht=instagram.fist4-1.fna.fbcdn.net&_nc_cat=106&_nc_ohc=-rc6T_BmqosAX-Ikj1_&edm=AOQ1c0wBAAAA&ccb=7-5&oh=00_AfC61SR_TyWRTcNvPD1mA-2XCY61avGbReagCn_WZVGUDA&oe=6420E222&_nc_sid=8fd12b"
        )
    )

    fun getGroups(): List<Group> {
        return groups
    }

    fun getPost(): ArrayList<Post> {
        return posts
    }

    fun getUser(): ArrayList<User> {
        return users
    }

    fun getComments(): List<Comment>? {
        return posts[0].comments
    }
}