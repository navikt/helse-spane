
# Algoritme for pseudovedtaksperioder (PVP)

**Eierskap**  
En PVP har eierskap til en subsumsjon dersom PVPen inneholder alle id-typene
(sid, søid, vpid) som subsumsjonen inneholder og inneholder subsumsjoner som 
matcher minst en gang med hver av id'ene til subsumsjonen. Dersom subsumsjonen 
inneholder vpid, er det tilstrekkelig at PVPen kun inneholder samme vpid

**Relevans**  
For å avgjøre om to subsumsjoner er relevante for hverandre må man se på tre mulige tilfeller.
Vi må se på hvilke id'er som er tilstede i subsumsjonen. Det finnes tre forskjellige muligheter
for hvilke id'er som kan være tilstede: 

1. Subsumsjonen inneholder Sykmeldingsid (sid), søknadsid (søid) og vedtaksperiodeid (vpid)
2. Subsumsjonen inneholder Sykmeldingsid (sid), søknadsid (søid)
3. Subsumsjonen inneholder Sykmeldingsid (sid)

Når man skal avgjøre om to subsumsjoner er relevante for hverandre, ser man på 
den subsumsjonen med færrest antall id'er og tar utgangspunkt i disse id'ene. Dette gir 
oss tre muligheter: 

1. Dersom man har alle id'ene må de to subsumsjonene matche på vpid
2. Dersom man har søid og sid må de to subsumsjonene matche på begge
3. Dersom man har sid må de to subsumsjonene matche på denne 

## Pseudovedtaksperiode (PVP)
En PVP inneholder:
- 0 eller 1 forskjellige vedtaksperiodeid'er
- et vilkårlig antall (0 - M) søknadsid'er
- minst en sykmeldingsid


Regler mellom PVPer   
- To forskjellige PVPer kan ikke inneholde samme vpid
- To forskjellige PVPer kan ikke inneholde samme søid

## Algoritme for å opprette PVPer 
Anta at det kommer inn en subsumsjon. Da skal dette følges stegvis for å gruppere 
denne subsumsjonen inn i en PVP eller opprette en ny PVP: 

1. Finnes det en PVP som har eierskap til subsumsjonen?  
- Dersom subsumsjonen kun inneholder sid og svaret er ja, legg subsumsjonen
inn i alle PVPer som har eierskap. Dersom dette skjer, går man ikke videre 
fra dette steget.
- Dersom subsumsjonen inneholder mer enn sid og svaret er ja, legg subsumsjonen
inn i PVPen som har eierskap
- Dersom svaret er nei, opprett en ny PVP og legg subsumsjonen inn i denne

2. Finnes det noen andre PVPer som har subsumsjoner som er relevante for PVPen
den nye subsumsjonen ble plassert i? 
- Dersom svaret er ja, legg til alle disse subsumsjonene i den nye PVPen

3. Fjern alle subsumsjoner fra andre PVPer som har søid'er som eies av den nye PVPen
