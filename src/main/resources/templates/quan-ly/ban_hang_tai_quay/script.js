document.addEventListener('DOMContentLoaded', () => {
    const productList = document.getElementById('product-list');
    const invoiceList = document.getElementById('invoice-list');
    const customerForm = document.getElementById('customer-form');
    const addProductForm = document.getElementById('add-product-form');

    let currentInvoice = null;

    function fetchProducts() {
        fetch('/api/products')
            .then(response => response.json())
            .then(data => {
                productList.innerHTML = '';
                data.forEach(product => {
                    const li = document.createElement('li');
                    li.classList.add('list-group-item');
                    li.innerHTML = `
                        ${product.name} - ${product.price.toLocaleString()} VND
                        <button class="btn btn-sm btn-primary" onclick="openAddProductModal(${product.id})">Thêm vào hóa đơn</button>
                    `;
                    productList.appendChild(li);
                });
            });
    }

    function fetchInvoices() {
        fetch('/api/invoices')
            .then(response => response.json())
            .then(data => {
                invoiceList.innerHTML = '';
                data.forEach(invoice => {
                    let total = 0;
                    const li = document.createElement('li');
                    li.classList.add('list-group-item');
                    li.innerHTML = `
                        <strong>Hóa đơn #${invoice.id}</strong> - Khách hàng: ${invoice.customerName} (${invoice.customerPhone})
                        <ul class="list-group mt-2">
                            ${invoice.items.map(item => {
                        total += item.price * item.quantity;
                        return `
                                    <li class="list-group-item">
                                        ${item.name} - ${item.price.toLocaleString()} VND x ${item.quantity}
                                        <button class="btn btn-sm btn-danger float-right" onclick="removeFromInvoice(${invoice.id}, ${item.id})">Xóa</button>
                                    </li>
                                `;
                    }).join('')}
                        </ul>
                        <div class="mt-2"><strong>Tổng tiền:</strong> ${total.toLocaleString()} VND</div>
                        <button class="btn btn-success mt-2" onclick="checkout(${invoice.id})">Thanh toán</button>
                    `;
                    invoiceList.appendChild(li);
                });
            });
    }

    window.openAddProductModal = function(productId) {
        $('#addProductModal').modal('show');
        addProductForm.onsubmit = function(event) {
            event.preventDefault();
            const quantity = document.getElementById('product-quantity').value;
            if (quantity && quantity > 0) {
                fetch(`/api/products/${productId}`)
                    .then(response => response.json())
                    .then(product => {
                        const invoiceItem = { ...product, quantity: parseInt(quantity) };
                        currentInvoice.items.push(invoiceItem);
                        fetch(`/api/invoices/${currentInvoice.id}`, {
                            method: 'PUT',
                            headers: {
                                'Content-Type': 'application/json'
                            },
                            body: JSON.stringify(currentInvoice)
                        })
                            .then(response => response.json())
                            .then(() => {
                                fetchInvoices();
                                $('#addProductModal').modal('hide');
                            });
                    });
            } else {
                alert('Số lượng không hợp lệ');
            }
        };
    };

    customerForm.addEventListener('submit', function(event) {
        event.preventDefault();

        const customerName = document.getElementById('customer-name').value;
        const customerPhone = document.getElementById('customer-phone').value;

        const newInvoice = {
            customerName: customerName,
            customerPhone: customerPhone,
            items: []
        };

        fetch('/api/invoices', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(newInvoice)
        })
            .then(response => response.json())
            .then(invoice => {
                currentInvoice = invoice;
                fetchInvoices();
                customerForm.reset();
            });
    });

    window.removeFromInvoice = function(invoiceId, productId) {
        fetch(`/api/invoices/${invoiceId}`)
            .then(response => response.json())
            .then(invoice => {
                invoice.items = invoice.items.filter(item => item.id !== productId);
                fetch(`/api/invoices/${invoiceId}`, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(invoice)
                })
                    .then(() => fetchInvoices());
            });
    };

    window.checkout = function(invoiceId) {
        fetch(`/api/invoices/${invoiceId}`)
            .then(response => response.json())
            .then(invoice => {
                if (confirm(`Xác nhận thanh toán hóa đơn #${invoice.id} với tổng tiền ${invoice.total.toLocaleString()} VND?`)) {
                    fetch(`/api/invoices/${invoiceId}`, {
                        method: 'DELETE'
                    })
                        .then(() => {
                            fetchInvoices();
                            alert('Thanh toán thành công!');
                        });
                }
            });
    };

    fetchProducts();
    fetchInvoices();
});
