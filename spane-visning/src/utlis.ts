import {PersonDto} from "./types";

export function byggStringRekursivt(innhold: any, antallInnrykk: number = 0) {
    const innrykk = " "
    if (innhold === null) {
        return innrykk.repeat(antallInnrykk) + "null\n"

    } else if (typeof innhold === "number") {
        return innrykk.repeat(antallInnrykk) + innhold.toString() + "\n"

    } else if (typeof innhold === "string" || typeof innhold === "boolean") {
        return innrykk.repeat(antallInnrykk) + innhold + "\n"

    } else if (Array.isArray(innhold)) {
        let resultat = ""
        for (const elem of innhold) {
            resultat += innrykk.repeat(antallInnrykk) + byggStringRekursivt(elem, antallInnrykk + 1)
        }
        return resultat

    } else if (typeof innhold === "object") {
        let resultat = ""
        for (const [key, value] of Object.entries(innhold)) {
            resultat += typeof value === "object" && value
                ? key + ":\n" + innrykk.repeat(antallInnrykk) + byggStringRekursivt(value, antallInnrykk) + "\n"
                : innrykk.repeat(antallInnrykk) + key + ": " + byggStringRekursivt(value, antallInnrykk);
        }
        return resultat

    } else {
        return "****** Ikke gjenkjent type, noe har skjedd feil ******"

    }
}



export function filtrerPseudovedtaksperioder(person: PersonDto, valgte: string[], fraDato: string, tilDato: string) {
    let pseudovedtaksperioder = [...person.vedtaksperioder]
    if (valgte.length > 0) {
        pseudovedtaksperioder = pseudovedtaksperioder.filter(vedtaksperiode =>
            valgte.includes(vedtaksperiode.orgnummer))
    }
    if (fraDato !== "" || tilDato !== "") pseudovedtaksperioder = pseudovedtaksperioder.filter(vedtaksperiode => vedtaksperiode.skjæringstidspunkt !== "ukjent")
    if (fraDato !== "") {
        let dato = new Date(fraDato)
        pseudovedtaksperioder = pseudovedtaksperioder.filter(vedtaksperiode => new Date(vedtaksperiode.skjæringstidspunkt) >= dato)
    }
    if (tilDato !== "") {
        let dato = new Date(tilDato)
        pseudovedtaksperioder = pseudovedtaksperioder.filter(vedtaksperiode => new Date(vedtaksperiode.skjæringstidspunkt) <= dato)
    }
    return pseudovedtaksperioder
}