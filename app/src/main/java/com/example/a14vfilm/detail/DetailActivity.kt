package com.example.a14vfilm.detail

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a14vfilm.R
import com.example.a14vfilm.adapters.DetailAdapter
import com.example.a14vfilm.models.Film
import java.util.*

class DetailActivity : AppCompatActivity() {
    var RVDetail: RecyclerView? = null
    var BTNFav: Button? = null
    var BTNOrder: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        RVDetail = findViewById(R.id.RVDetail)
        val film1 = Film(1, "1", "B5314. Scream 2022 - Tiếng Thét 2022 2D25G (DTS-HD MA 7.1)", "Đạo diễn phim kinh dị 'Last Night in Soho' viết tâm thư yêu cầu khán giả không tiết lộ nội dung phim Trong bức thư đăng tải trên Instagram, đạo diễn Edgar Wright cho biết ông và ê kíp phim Last Night in Soho (tạm dịch: Đêm qua ở Soho) đã quyết định dời ngày chiếu tác phẩm đến ngày 29.10 tới tại các rạp ở thị trường Mỹ, nhưng do tác phẩm chiếu ra mắt tại Liên hoan phim Venice ở Ý ngày 4.9 vừa qua (giờ địa phương) nên có nhiều khán giả sẽ xem trước phim này. Last Night In Soho: Phim kinh dị không hù dọa nhưng vẫn gây ám ảnh tột độ Do đó, đạo diễn bộc bạch trong thư: \"Bản thân tôi và đoàn làm phim Last Night in Soho rất hứng khởi khi phim chiếu ra mắt tại Liên hoan phim Venice nhưng mong mọi người, nhất là những khán giả đầu tiên của chúng tôi đừng tiết lộ về phim để khán giả xem sau có cơ hội khám phá tác phẩm. Last Night in Soho của Edgar Wright là phim kinh dị tâm lý, được đánh giá là mang đến cảm giác ớn lạnh “nặng đô”, đủ sức gây ám ảnh đến bất cứ tín đồ phim kinh dị sừng sỏ nào. Bộ phim là một sự khởi đầu quan trọng đối với Edgar Wright. Mặc dù vị đạo diễn đã từng làm nhiều phim kị nhưng chỉ đến khi thực hiện Last Night In Soho mới thực sự xem là thành công khi hiện tại, tác phẩm có 71% đánh giá tốt từ giới phê bình trên Rotten Tomatoes và 94% từ phía khán giả. Đạo diễn Edgar Wright chia sẻ về quá trình sáng tạo này: “Ý tưởng cho bộ phim Last Night In Soho là kết quả của 25 năm sống và làm việc ở Soho. Tôi đã dành rất nhiều thời gian để ngắm nhìn các kiến trúc ở đây và nghĩ: Những bức tường này đã nhìn thấy gì? Khi đi dạo trên phố vào đêm khuya, Soho trở nên dịu dàng hơn rất nhiều, nhưng nó vẫn còn bóng tối dưới bề mặt. Đó là một trong những nơi mà bạn chỉ cần đứng yên trong 60 giây để điều gì đó kỳ lạ hoặc huyền hoặc, đen tối xảy ra.” Bộ phim là sự đan xen giữa bối cảnh hiện đại và khung cảnh hoài niệm những năm 1960. Ellie (Thomasin McKenzie) là một cô gái nông thôn ngọt ngào, sống trong bảo bọc từ nhỏ và mơ ước trở thành nhà thiết kế thời trang. Khi được nhận vào Đại học Thời trang London, cô chuyển đến thành phố lớn với hy vọng đây sẽ là bước ngoặc cho sự nghiệp của mình. Trớ trêu thay, nó lại trở thành sự khởi đầu của một cơn ác mộng khi Ellie phát hiện mình có sự liên kết kì lạ với cô gái có dính líu tới án mạng tên Sandie (Anya Taylor-Joy) sống ở London những năm 1960. Theo Pa Bích thấy, đó là một tiền đề hấp dẫn dẫn dắt đến những tình tiết vô cùng đáng sợ. Tuy nhiên, nói chính xác hơn thì Last Night In Soho mang hơi hướng kinh dị nhiều hơn là một bộ phim thực sự thuộc thể loại này, kiểu như phim trinh tội phạm thám ấy, tức là phim sẽ gây ám ảnh thông qua âm thanh, cảnh phim đáng sợ hoặc diễn biến tâm lý, hành động ma quái của nhân vật chứ không lạm dụng những cảnh hù dọa chí mạng như những phim kinh dị truyền thống. Những khoảnh khắc ớn lạnh đa số tập trung vào sự xuất hiện của những bóng ma kì quái mà Ellie nhìn thấy. Những thứ này có khuôn mặt mờ xám xịt với đôi mắt đen không rõ ràng. Mặc dù có sự xuất hiện của những bóng ma nhưng Last Night in Soho không diễn tả chúng đáng sợ quá mức như Valak hay Annabelle đâu. Tuy nhiên, điều đó không có nghĩa là Last Night in Soho không gây ám ảnh. Câu chuyện buồn của ca sĩ đầy khát vọng Sandie liên quan đến việc cô bị lạm dụng bởi những gã đàn ông giàu có trong câu lạc bộ ngầm ở London những năm 1960, được giới thiệu bởi người quản lý - bạn trai của cô, Jack (Matt Smith). Trong phim có rất nhiều cảnh bạo lực và “mây mưa\", từ việc Jack đối xử với Sandie như một món hàng cho đến việc cô phải phục vụ những gã đàn ông giấu mặt để kiếm tiền cho tên bạn trai sở khanh. Một cảnh khác ở phần đầu của bộ phim cũng tạo tiền đề cho chủ đề xuyên suốt phim là “phản đối nạn bạo lực và xâm hại với phụ nữ” khi Ellie ngây thơ bị một gã tài xế taxi điên loạn dồn đường cùng để thực hiện mưu đồ xấu xa. Bên cạnh đó, phim cũng có một số cảnh máu me, tuy nhiên, máu không được diễn tả màu thật mà mang sắc đỏ tươi. Các nhà làm phim về thể loại giallo (tâm lý tội phạm và kinh dị bí ẩn) thường dùng những cảnh máu me để tạo nên khung cảnh mang tính thẩm mỹ chứ không nhắm đến mục đích hù dọa. Nói chung, bất cứ ai xem Last Night In Soho mà mang tâm lý mong đợi một bộ phim kinh dị rùng rợn truyền thống thì có thể thất vọng. Đơn giản vì như Pa Bích đã nói ở trên, phim của Edgar Wright tập trung vào việc tạo bầu không khí căng thẳng và làm mờ ranh giới giữa giả tưởng và thực tế hơn là hù dọa. Last Night In Soho còn là một bộ phim trau chuốt về hình ảnh và phong cách nhằm tôn vinh thể loại giallo và phim kinh dị kịch tính của những năm 1960 và 1970, do đó, kinh dị thiên về tâm lý hơn là xác thịt. Tuy nhiên, những người nhạy cảm với cảnh bạo lực hoặc “mây mưa” thác loạn có thể muốn tránh xa Last Night In Soho. Nó có thể không phải là một bộ phim kinh dị truyền thống, nhưng vẫn có những yếu tố gây khó chịu và ám ảnh. Nếu không ngại những yếu tố trên, Last Night In Soho đích thị là bộ phim mà bạn nên thưởng thức.", 10, 3, "Movie", 120, 0, "Mỹ", Date(2021, 12, 20), 100000.0, 10, Date(2021, 3, 27))
        val detailAdapter = DetailAdapter(arrayListOf(film1))
        RVDetail!!.adapter = detailAdapter
        RVDetail!!.layoutManager = LinearLayoutManager(this)

        BTNFav = findViewById(R.id.BTNFavourite)
        BTNFav!!.setBackgroundColor(Color.TRANSPARENT)
        BTNFav!!.setTextColor(Color.RED)

        BTNOrder = findViewById(R.id.BTNOrder)
        BTNOrder!!.setBackgroundColor(Color.BLUE)

    }
}