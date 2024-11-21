const inquiryManage = () => {
    window.location.href = '/admin/inquiries'
}

const inquiryAnswerSave = async () => {

    const inquiryId = document.getElementById('inquiry').querySelector('input[type="hidden"]').value;
    const adminContent = document.querySelector('textarea').value;

    const item = {
        inquiryId,
        adminContent
    }

    const response = await postJsonResponse('/api/inquiries/save', JSON.stringify(item));

    if (response.status === 200) {
        alert('저장완료')
        window.location.href;
    } else {
        alert('저장실패')
    }

}

const inquiryAnswerModifyForm = () => {
    const inquiryId = document.getElementById('inquiry').querySelector('input[type="hidden"]').value;
    window.location.href = `/admin/inquiries/modifyform/${inquiryId}`
}

const inquiryAnswerModify = async () => {
    const inquiryAnswerId = document.getElementById('inquiryAnswer').querySelector('input[type="hidden"]').value;
    const adminContent = document.querySelector('textarea').value;

    const item = {
        inquiryAnswerId,
        adminContent
    }

    const response = await postJsonResponse('/api/inquiries/modify', JSON.stringify(item));

    if (response.status === 200) {
        const data = await response.text();
        console.log(data);
        alert('변경완료')
        window.location.href = `/admin/inquiries/detail/${inquiryAnswerId}`
    } else {
        alert('잘못된요청입니다.')
    }

}

const inquiryList = () => {
    window.location.href = `/admin/inquiries`
}

const noAnswerInquiryList = () => {
    window.location.href = `/admin/inquiries/noanswer`
}