export type PersonDto = {
    vedtaksperioder: VedtaksperiodeDto[];
    fnr: string;
};

export type VedtaksperiodeDto = {
    subsumsjoner: SubsumsjonDto[];
};

export type SubsumsjonDto = {
    id: string;
    versjon: string;
    eventName: string;
    kilde: string;
    versjonAvKode: string;
    fødselsnummer: string;
    sporing: Map<string, string[]>;
    tidsstempel: string;
    lovverk: string;
    lovverksversjon: string;
    paragraf: string;
    ledd: number | null;
    punktum: number | null;
    bokstav: string | null;
    input: Map<string, any>;
    output: Map<string, any>;
    utfall: string;
};