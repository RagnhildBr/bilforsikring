document.getElementById('purchaseForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const request = {
        customerId: document.getElementById('customerId').value,
        registrationNumber: document.getElementById('registrationNumber').value,
        bonus: document.getElementById('bonus').value
    };
    
    const response = await fetch('/api/insurance-purchases', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(request)
    });
    
    const result = await response.json();
    document.getElementById('result').innerText = JSON.stringify(result, null, 2);
});
