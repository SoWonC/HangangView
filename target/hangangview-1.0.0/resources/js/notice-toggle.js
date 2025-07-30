var isNoticesVisible = false;

function toggleNotices() {
    if (isNoticesVisible) {
        $('#noticeArea').html('');
        isNoticesVisible = false;
    } else {
        $.ajax({
            type: 'GET',
            url: '/project_1108/notices',
            success: function (response) {
                $('#noticeArea').html(response);
                isNoticesVisible = true;
            },
            error: function () {
                alert('공지사항 목록을 불러오는 데 실패했습니다.');
            }
        });
    }
}
