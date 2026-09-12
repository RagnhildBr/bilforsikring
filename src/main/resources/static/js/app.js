document.getElementById('purchaseForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const request = {
        firstName: document.getElementById('firstName').value,
        lastName: document.getElementById('lastName').value,
        personalNumber: document.getElementById('personalNumber').value,
        email: document.getElementById('email').value,
        registrationNumber: document.getElementById('registrationNumber').value,
        bonus: document.getElementById('bonus').value
    };
    
    const response = await fetch('/api/insurance-purchases', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(request)
    });
    
    if (response.ok) {
        const result = await response.json();
        document.getElementById('result').innerHTML = `
            <div style="background: #e6f4ea; border: 1px solid #1e7e34; padding: 16px; border-radius: 4px; margin-top: 24px;">
                <h3 style="margin-top: 0; color: #1e7e34;">Forsikring kjøpt!</h3>
                <p><strong>Policy ID:</strong> ${result.policyId}</p>
                <p><strong>Status:</strong> ${result.status}</p>
                <p>${result.message}</p>
            </div>
        `;
        document.getElementById('purchaseForm').reset();
    } else {
        const errorData = await response.json();
        document.getElementById('result').innerHTML = `
            <div style="background: #fdf2f2; border: 1px solid #d13b3b; padding: 16px; border-radius: 4px; margin-top: 24px; color: #d13b3b;">
                <h3 style="margin-top: 0;">Feil ved kjøp</h3>
                <pre>${JSON.stringify(errorData, null, 2)}</pre>
            </div>
        `;
    }
});
