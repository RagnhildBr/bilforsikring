# Kjøp av bilforsikring

En forenklet løsning for kjøp av bilforsikring. Løsningen er utviklet som en oppgave til teknisk intervju og demonstrerer en fullstendig flyt fra frontend til backend med validering, forretningslogikk og mock-integrasjoner.

## Teknologi

- **Backend:** Kotlin 2.2.0, Spring Boot 4.1.1
- **Java-målversjon:** 17
- **Frontend:** HTML, CSS og vanilla JavaScript
- **Byggeverktøy:** Maven med Maven Wrapper
- **Testing:** JUnit og Mockito

## Slik kjører du prosjektet

### Forutsetninger
- JDK 26 og Maven installert.
- Prosjektet er konfigurert med Java 17 som målversjon.

### Starte applikasjonen

```bash
mvn spring-boot:run
```

Når applikasjonen har startet, kan du åpne forsikringsskjemaet i nettleseren på:
[http://localhost:8080](http://localhost:8080)

### Kjøre tester
For å kjøre alle enhetstester og integrasjonstester:

```bash
mvn test
```

## API-oversikt

Løsningen tilbyr følgende endepunkter:

- `POST /api/insurance-purchases`: Hovedendepunkt for gjennomføring av et komplett forsikringskjøp.

### Eksempel på forespørsel (Kjøp)
```json
{
  "firstName": "Ola",
  "lastName": "Nordmann",
  "personalNumber": "12345678901",
  "email": "ola@nordmann.no",
  "registrationNumber": "AB12345",
  "bonus": "50"
}
```

## Arkitektur og Design

### Frontend
- Design basert på tilsendte skisser.
- Bruker standard HTML-validering supplert med JavaScript for API-kommunikasjon.
- Dynamisk feilhåndtering som viser valideringsfeil fra backend.

### Backend
- **Controller-lag:** Håndterer HTTP-forespørsler og inngående validering (JSR-303).
- **Service-lag:** Koordinerer forretningslogikken, inkludert kundeopprettelse, draft-håndtering og aktivering av poliser.
- **Integrasjons-lag:** Simulerer eksterne systemer (Policy-system og Brev-tjeneste) ved bruk av in-memory lagring (`ConcurrentHashMap`) og mock-implementasjoner.
- **Feilhåndtering:** Sentralisert `ApiExceptionHandler` som mapper unntak til strukturerte JSON-svar med korrekte HTTP-statuskoder.

## Avgrensninger

- Løsningen bruker in-memory lagring. Data går tapt når applikasjonen stoppes.
- Forsikringsproduktet er forenklet til en standard bilforsikring.
- Systemet simulerer utsendelse av bekreftelsesbrev via konsolllogging.