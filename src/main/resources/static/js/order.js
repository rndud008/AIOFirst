const orderManage = () => {
    window.location.href = '/admin/orders'
}

const orderStatusChange = async (button, status = "") => {

    let div = button.closest('div').parentNode;
    const modal = document.getElementById('modal');

    if (modal !== null){
        div = modal.parentNode;
    }

    const orderId = div.querySelector("input[type='hidden']").value;

    if (status === ''){
        const option = Array.from(document.querySelectorAll('option'));
        option.forEach(option =>{
            if (option.selected){
                status = option.value;
            }
        })
    }

    const item = {
        orderId,
        status
    }

    const response = await postJsonResponse(`/api/orders/status`, JSON.stringify(item))


    if (response.status === 200) {
        window.location.href = window.location.href
        alert("변경완료")
    } else {
        alert("잘못된요청입니다.")
    }

}

const getOrderList = async (status = "") => {

    const response = await getResponse(`/api/orders?status=${status}`)

    const data = await response.json();

    if (response.status === 200) {
        console.log(data)
        console.log(data.length)
        const ordersDiv = document.getElementById('orders');
        if (data.length === 0) {
            ordersDiv.innerHTML = `<div class="orderItem">주문 내역이 존재하지 않습니다.</div>`
        } else {
            let html = "";

            data.forEach(order => {
                html +=
                    `
                        <div class="orderItem">
                            <input type="hidden" value="${order.orderId}">
                            <div class="orderIndex"><span>${order.index}</span></div>
                            <div class="orderProductName">
                                <a  href='/admin/orders/detail/${order.orderId}' >
                                   `
                order.orderItemDTOS.forEach(orderItem => {
                    html +=
                        `
                                    <span>
                                        <span>${orderItem.productName}</span>
                                        <span>${orderItem.option}</span>
                                    </span>
                                `
                })
                html +=
                    `
                         </a>
                            </div>
                            <div class="orderTotal"><span >${order.totalPrice}</span></div>
                            <div class="orderStatus"><span id="orderStatus">${order.status}</span></div>
                            <div class="orderDate"><span >${order.createdAt}</span></div>
                            <div class="orderAction">
                             `
                if (order.itemPending) {
                    html += `<button onclick="orderStatusChange(this,'PREPARING_ITEM')">주문확인</button>`
                }
                if (order.adminCheck) {
                    html += `<button onclick="orderStatusChangeModal(this)">주문상태변경</button>`
                }
                html +=
                    `
                        </div>
                        </div>
                        `
            })

            ordersDiv.innerHTML = html;
        }

    } else {
        alert("잘못된 요청입니다.")
    }

}