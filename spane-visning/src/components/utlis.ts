export default function byggStringRekursivt(innhold: any, resultat: string = "") {
    if (innhold === null) {
        resultat += "null\n"
    } else if (typeof innhold === "string") {
        resultat += innhold + "\n"
    } else if (typeof innhold === "boolean") {
        resultat += innhold + "\n"
    } else if (typeof innhold === "number") {
        resultat += innhold.toString() + "\n"
    } else if (Array.isArray(innhold)) {
        for (const elem of innhold) {
            resultat += byggStringRekursivt(elem) + "\n"
        }
    } else if (typeof innhold === "object") {
        for (const [key, value] of Object.entries(innhold)) {
            if (typeof value === "object" && value) {
                resultat += key + ":\n" + byggStringRekursivt(value) + "\n"
            } else {
                resultat += key + ": " + byggStringRekursivt(value) + "\n"
            }
        }
    } else {
        resultat += "****** Ikke gjenkjent type, noe har skjedd feil ******"
    }
    return resultat
}