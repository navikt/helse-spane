export type PersonDto = {
  vedtaksperioder: VedtaksperiodeDto[];
  fnr: string;
};

export type ParagrafsøkDto = {
  personer: PersonDto[];
};

export type VedtaksperiodeDto = {
  subsumsjoner: SubsumsjonDto[];
  orgnummer: string;
  skjæringstidspunkt: string | null;
  tilstand: string;
  ikkeSikkertSkjæringstidspunkt: Boolean;
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
