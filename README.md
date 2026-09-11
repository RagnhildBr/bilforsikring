# Kjøp av bilforsikring

En forenklet løsning for kjøp av bilforsikring. Løsning er utviklet som en
oppgave til teknisk intervju. Den er ikke komplett, og sikter kun å gi grunnlag for videre diskusjon. 

## Fokus og omfang

Hovedvekten er lagt på backend og REST-API-et.

Klienten holdes enkel og demonstrerer innsending av skjemadata,
validering og håndtering av svar fra API-et.

## Avgrensninger og antakelser

- Løsningen gjelder kjøp av ansvarsforsikring for én bil.
- Forsikringsproduktet er forhåndsvalgt.
- Alle feltene i skissen er obligatoriske.
- Prisberegning, betaling og innlogging er utenfor omfanget.
- Kjøpet regnes som vellykket når avtalen er aktivert og
  brevtjenesten har akseptert bestillingen av bekreftelsesbrevet.
- Klienten sender én forespørsel og mottar enten en
  kjøpsbekreftelse med avtale-ID eller en feilmelding.