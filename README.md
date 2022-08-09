# 🕵️ helse-spane 🕵️

Spane forsøker gjøre visning og søk i juridiske vurderinger fra
[Helse Etterlevelse sitt topic](https://github.com/navikt/omrade-helse-etterlevelse-topic), som er gjort automatisk med
referanse til paragraf og med relevante input- og output-data, for å kunne spore opp gitte vurderinger gjort på et
vedtak, eller vedtak berørt av en vurdering.

## Komme i gang

Applikasjonen bruker Gradle for å bygge backenden. Kjør kommandoen

`./gradlew build`

for å bygge applikasjonen. Applikasjonen krever Docker for å kjøre integrasjonstestene. 


For å starte opp frontenden, skriv

`npm i && npm run dev`

i terminalen. Dette vil installere de nødvendige avhengighetene og så kjøre opp frontenden lokalt med testdata.


## Applikasjonens inndeling

Applikasjonen er delt inn i tre deler: 
- spane-mediators
  - Lesing fra Kafka-strøm og skriving til database. 
- spane-model
  - Modellene som er brukt for å representere personene og deres vedtaksperioder. 
- spane-visning. 
  - Frontenden til applikasjonen.

## Henvendelser

Spørsmål knyttet til koden eller prosjektet kan stilles som issues her på GitHub

## For NAV-ansatte

Interne henvendelser kan sendes via Slack i kanalen #team-bømlo-værsågod.
