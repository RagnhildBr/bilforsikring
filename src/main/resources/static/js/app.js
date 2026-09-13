const form = document.getElementById('purchaseForm');
const resultContainer = document.getElementById('result');
const fields = Array.from(form.querySelectorAll('input, select'));

let submitting = false;

const validationMessages = {
    registrationNumber: {
        valueMissing: 'Vennligst oppgi registreringsnummer.'
    },
    bonus: {
        valueMissing: 'Vennligst velg bonus.'
    },
    personalNumber: {
        valueMissing: 'Vennligst oppgi fødselsnummer.',
        patternMismatch: 'Fødselsnummer må bestå av 11 siffer.'
    },
    firstName: {
        valueMissing: 'Vennligst oppgi fornavn.'
    },
    lastName: {
        valueMissing: 'Vennligst oppgi etternavn.'
    },
    email: {
        valueMissing: 'Vennligst oppgi e-postadresse.'
    }
};

function getErrorSpan(input) {
    return (input.closest('.field') || input.parentElement)
        .querySelector('.field-error');
}

function displayFieldError(input, message) {
    input.classList.add('invalid');
    input.setAttribute('aria-invalid', 'true');

    let errorSpan = getErrorSpan(input);

    if (!errorSpan) {
        errorSpan = document.createElement('span');
        errorSpan.className = 'field-error';
        input.insertAdjacentElement('afterend', errorSpan);
    }

    if (!errorSpan.id) {
        errorSpan.id = `${input.id}Error`;
    }

    const describedBy = new Set(
        (input.getAttribute('aria-describedby') || '')
            .split(/\s+/)
            .filter(Boolean)
    );

    describedBy.add(errorSpan.id);
    input.setAttribute('aria-describedby', [...describedBy].join(' '));

    errorSpan.textContent = message;
}

function clearFieldError(input) {
    input.setCustomValidity('');
    input.classList.remove('invalid');
    input.removeAttribute('aria-invalid');

    const errorSpan = getErrorSpan(input);

    if (errorSpan) {
        errorSpan.textContent = '';
    }
}

function clearErrors() {
    fields.forEach(clearFieldError);
}

// Native validation uses the constraints in your HTML:
// required, type="email", pattern, etc.
fields.forEach(input => {
    const messages = validationMessages[input.id] || {};

    input.addEventListener('invalid', () => {
        // Clear previous custom errors before checking native validity.
        input.setCustomValidity('');

        const validity = input.validity;
        let message = '';

        if (validity.valueMissing) {
            message =
                messages.valueMissing ||
                'Vennligst fyll ut dette feltet.';
        } else if (validity.patternMismatch) {
            message =
                messages.patternMismatch ||
                'Ugyldig format.';
        } else if (
            validity.typeMismatch &&
            input.type === 'email'
        ) {
            message = 'Skriv en gyldig e-postadresse.';
        } else if (!validity.valid) {
            message = 'Ugyldig verdi.';
        }

        input.setCustomValidity(message);

        if (message) {
            displayFieldError(input, message);
        }
    });

    // Editing removes errors associated with the previous value.
    input.addEventListener('input', () => clearFieldError(input));
    input.addEventListener('change', () => clearFieldError(input));
});

function showFieldErrors(errors) {
    const form = document.getElementById('purchaseForm');
    const inputs = Array.from(form.querySelectorAll('input, select'));
    let firstInvalidInput = null;

    for (const input of inputs) {
        const message = errors?.[input.id];

        if (typeof message !== 'string' || !message) continue;

        // Backend-feilen vises inline, uten å låse browser-valideringen.
        input.setCustomValidity('');
        input.classList.add('invalid');
        input.setAttribute('aria-invalid', 'true');

        const container = input.closest('.field') || input.parentElement;
        const errorSpan = container.querySelector('.field-error');

        if (errorSpan) {
            errorSpan.textContent = message;
        }

        firstInvalidInput ??= input;
    }

    firstInvalidInput?.focus();
    return firstInvalidInput !== null;
}

function showResult(
    title,
    message,
    success = false,
    policyId = null
) {
    const panel = document.createElement('div');

    panel.style.cssText = `
        background: ${success ? '#e6f4ea' : '#fdf2f2'};
        border: 1px solid ${success ? '#1e7e34' : '#d13b3b'};
        color: ${success ? '#1e7e34' : '#d13b3b'};
        padding: 24px;
        border-radius: 4px;
        margin-top: 24px;
    `;

    const heading = document.createElement('h3');
    heading.style.marginTop = '0';
    heading.textContent = title;

    const paragraph = document.createElement('p');
    paragraph.textContent = message;

    panel.append(heading, paragraph);

    if (policyId !== null) {
        const reference = document.createElement('p');
        reference.textContent = `Polisenummer: ${policyId}`;
        panel.append(reference);
    }

    // textContent avoids interpreting server messages as HTML.
    resultContainer.replaceChildren(panel);
}

form.addEventListener('reset', () => {
    clearErrors();
    resultContainer.replaceChildren();
});

form.addEventListener('submit', async event => {
    event.preventDefault();

    if (submitting) {
        return;
    }

    clearErrors();
    resultContainer.replaceChildren();

    const value = id => document.getElementById(id).value.trim();
    const bonus = value('bonus');

    const request = {
        firstName: value('firstName'),
        lastName: value('lastName'),
        personalNumber: value('personalNumber'),
        email: value('email'),
        registrationNumber: value('registrationNumber')
            .replace(/\s/g, ''),
        bonus: bonus === '' ? null : Number.parseInt(bonus, 10)
    };

    // Prevent changes while the request is in progress, so a response
    // cannot display errors for values the user has already changed.
    const controls = Array.from(form.elements);
    const previousDisabled = controls.map(control => control.disabled);

    submitting = true;
    controls.forEach(control => {
        control.disabled = true;
    });
    form.setAttribute('aria-busy', 'true');

    let response;
    let data;

    try {
        response = await fetch('/api/insurance-purchases', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify(request)
        });

        const body = await response.text();

        // A non-JSON response should not be labelled a network failure.
        try {
            data = body ? JSON.parse(body) : null;
        } catch {
            data = null;
        }
    } catch {
        showResult(
            'Kjøpet kunne ikke bekreftes',
            'Forbindelsen til serveren ble avbrutt. Kjøpet kan ha ' +
            'blitt registrert. Avklar status før du forsøker på nytt.'
        );

        return;
    } finally {
        controls.forEach((control, index) => {
            control.disabled = previousDisabled[index];
        });

        submitting = false;
        form.removeAttribute('aria-busy');
    }

    if (response.ok) {
        // Reset before showing confirmation, because reset clears results.
        form.reset();

        showResult(
            'Takk for ditt kjøp!',
            'Kjøpet av bilforsikring er gjennomført.',
            true,
            data?.policyId ?? null
        );

        resultContainer.scrollIntoView({
            behavior: 'smooth',
            block: 'start'
        });
    } else if (
        response.status === 400 &&
        showFieldErrors(data)
    ) {
        // Expected backend format:
        // { firstName: "Fornavn kan kun inneholde bokstaver" }
        return;
    } else {
        showResult(
            'Kjøpet kunne ikke bekreftes',
            typeof data?.message === 'string'
                ? data.message
                : 'Serveren kunne ikke bekrefte kjøpet. ' +
                'Avklar status før du forsøker på nytt.'
        );
    }
});

function clearErrorOnEdit(event) {
    const input = event.target;

    if (!input.matches('input, select')) return;

    input.setCustomValidity('');
    input.classList.remove('invalid');
    input.removeAttribute('aria-invalid');

    const container = input.closest('.field') || input.parentElement;
    const errorSpan = container.querySelector('.field-error');

    if (errorSpan) {
        errorSpan.textContent = '';
    }
}

document.getElementById('purchaseForm')
    .addEventListener('input', clearErrorOnEdit);

document.getElementById('purchaseForm')
    .addEventListener('change', clearErrorOnEdit);