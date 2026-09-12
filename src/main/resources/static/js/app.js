document.getElementById('purchaseForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const form = e.target;
    const submitButton = form.querySelector('button[type="submit"]');
    const resultContainer = document.getElementById('result');

    // Reset UI state
    clearErrors();
    resultContainer.innerHTML = '';
    submitButton.disabled = true;

    const request = {
        firstName: document.getElementById('firstName').value,
        lastName: document.getElementById('lastName').value,
        personalNumber: document.getElementById('personalNumber').value,
        email: document.getElementById('email').value,
        registrationNumber: document.getElementById('registrationNumber').value.replace(/\s/g, ''),
        bonus: document.getElementById('bonus').value ? document.getElementById('bonus').value + '%' : null
    };

    try {
        const response = await fetch('/api/insurance-purchases', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(request)
        });

        if (response.ok) {
            const result = await response.json();
            resultContainer.innerHTML = `
                <div style="background: #e6f4ea; border: 1px solid #1e7e34; padding: 24px; border-radius: 4px; margin-top: 32px;">
                    <h3 style="margin-top: 0; color: #1e7e34; font-size: 24px;">Takk for ditt kjøp!</h3>
                    <p style="font-size: 18px;">Din bilforsikring er nå aktivert.</p>
                    <p style="margin-bottom: 0;"><strong>Policy ID:</strong> ${result.policyId}</p>
                </div>
            `;
            form.reset();
            window.scrollTo({ top: resultContainer.offsetTop - 20, behavior: 'smooth' });
        } else if (response.status === 400) {
            const errors = await response.json();
            showFieldErrors(errors);
        } else {
            const errorData = await response.json();
            resultContainer.innerHTML = `
                <div style="background: #fdf2f2; border: 1px solid #d13b3b; padding: 16px; border-radius: 4px; margin-top: 24px; color: #d13b3b;">
                    <h3 style="margin-top: 0;">Kunne ikke gjennomføre kjøp</h3>
                    <p>${errorData.message || 'Det oppstod en uventet feil. Vennligst prøv igjen senere.'}</p>
                </div>
            `;
        }
    } catch (error) {
        resultContainer.innerHTML = `
            <div style="background: #fdf2f2; border: 1px solid #d13b3b; padding: 16px; border-radius: 4px; margin-top: 24px; color: #d13b3b;">
                <h3 style="margin-top: 0;">Nettverksfeil</h3>
                <p>Kunne ikke kontakte serveren. Sjekk internettforbindelsen din.</p>
            </div>
        `;
    } finally {
        submitButton.disabled = false;
    }
});

function showFieldErrors(errors) {
    for (const [field, message] of Object.entries(errors)) {
        const input = document.getElementById(field);
        if (input) {
            input.classList.add('invalid');
            // Check if there is already a field-error span, or create one if needed
            let errorSpan = input.parentElement.querySelector('.field-error');
            if (errorSpan) {
                errorSpan.textContent = message;
            }
        }
    }
}

function clearErrors() {
    document.querySelectorAll('.invalid').forEach(el => el.classList.remove('invalid'));
    document.querySelectorAll('.field-error').forEach(el => {
        el.textContent = '';
        if (el.id === 'emailError') {
            el.textContent = 'Skriv en gyldig e-postadresse';
        }
    });
}
