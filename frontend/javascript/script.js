jQuery(function () {

  // 제목 클릭 → 모달
  $('.qna-title').on('click', function () {
    const title = $(this).text();
    const content = '질문 상세 내용입니다. (백엔드 연동 예정)';
    $('#modalTitle').text(title);
    $('#modalContent').text(content);
    $('#modal').addClass('active');
  });

  // 모달 닫기
  $('.btn').on('click', function () {
    $('#modal').removeClass('active');
  });

  // 글 등록
  $('#writeBtn').on('click', function () {
    const title = $('#title').val();
    const content = $('#content').val();

    if (!title || !content) {
      alert('제목과 내용을 입력하세요');
      return;
    }

    const rowCount = $('#qnaList tr').length + 1;

    $('#qnaList').prepend(`
      <tr>
        <td>${rowCount}</td>
        <td class="qna-title">${title}</td>
        <td>익명</td>
        <td>2025-12-29</td>
        <td><span class="status wait">대기</span></td>
      </tr>
    `);

    $('#title').val('');
    $('#content').val('');
  });

});
