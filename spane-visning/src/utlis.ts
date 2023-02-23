import {PersonDto} from "./types";

export function byggStringRekursivt(innhold: any, antallInnrykk: number = 0) {
    const innrykk = " "
    if (innhold === null) {
        return innrykk.repeat(antallInnrykk) + fornorsk(innhold) + "\n"
    } else if (typeof innhold === "number") {
        return innrykk.repeat(antallInnrykk) + innhold.toString() + "\n"
    } else if (typeof innhold === "string") {
        return innrykk.repeat(antallInnrykk) + innhold + "\n"
    } else if (typeof innhold === "boolean") {
        return innrykk.repeat(antallInnrykk) + fornorsk(innhold) + "\n"
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

function fornorsk(innhold: Boolean | null) {
    if (innhold == null) {
        return "Mangler"
    } else if (innhold) {
        return "Ja"
    } else {
        return "Nei"
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
        pseudovedtaksperioder = pseudovedtaksperioder.filter(vedtaksperiode => {
            let skjæringstidspunkt = vedtaksperiode.skjæringstidspunkt
            if (skjæringstidspunkt != null) {
                return new Date(skjæringstidspunkt) >= dato
            }
            return false
        })
    }
    if (tilDato !== "") {
        let dato = new Date(tilDato)
        pseudovedtaksperioder = pseudovedtaksperioder.filter(vedtaksperiode => {
            let skjæringstidspunkt = vedtaksperiode.skjæringstidspunkt
            if (skjæringstidspunkt != null) {
                return new Date(skjæringstidspunkt) <= dato
            }
            return false
        })
    }
    return pseudovedtaksperioder
}

export function formatDateString(dateString: string) {
    let dato = new Date(dateString);
    const visningsmåned = dato.getMonth() + 1
    let str = "";
    str +=
        dato.getDate().toString().length === 1
            ? "0" + dato.getDate()
            : dato.getDate();
    str += ".";
    str +=
        visningsmåned.toString().length === 1
            ? "0" + visningsmåned
            : visningsmåned;
    str += ".";
    str += dato.getFullYear();
    return str;
}

export function formatDateTimeString(dateTimeString: string) {
    let dato = new Date(dateTimeString);
    let str = formatDateString(dateTimeString)
    str += ", ";
    str +=
        dato.getHours().toString().length === 1
            ? "0" + dato.getHours()
            : dato.getHours();
    str += ":";
    str +=
        dato.getMinutes().toString().length === 1
            ? "0" + dato.getMinutes()
            : dato.getMinutes();
    str += ":";
    str +=
        dato.getSeconds().toString().length === 1
            ? "0" + dato.getSeconds()
            : dato.getSeconds();
    str += ".";
    str += dateTimeString.match(/\.(\d[^+]*)/)![1];
    return str;
}
