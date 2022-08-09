import testPerson from "./resources/testPerson.json";
import testPersoner from "./resources/testPersoner.json";
import { PersonDto } from "./types";

export const restBackendPerson = (): BackendPerson => {
  return {
    person(fnr: string): Promise<PersonDto> {
      return fetch(`/fnr/` + fnr, {
        method: "get",
        headers: {
          Accept: "application/json",
        },
      }).then((response) => {
        if(!response.ok){
          return null
        }
        return response.json()
      });
    },
  };
};

export const testBackendPerson = (): BackendPerson => {
  return {
    person(): Promise<PersonDto> {
     return Promise.resolve(testPerson as unknown as PersonDto);
    },
  };
};

export type BackendPerson = {
  person: (fnr: string) => Promise<PersonDto>;
};

export const restBackendParagraf = (): BackendParagraf => {
  return {
    personer(paragraf: string): Promise<PersonDto[]> {
      return fetch(`/paragraf/` + paragraf, {
        method: "get",
        headers: {
          Accept: "application/json",
        },
      }).then((response) => response.json());
    },
  };
};

export const testBackendParagraf = (): BackendParagraf => {
  return {
    personer(): Promise<PersonDto[]> {
      return Promise.resolve(testPersoner as unknown as PersonDto[]);
    },
  };
};

export type BackendParagraf = {
  personer: (paragraf: string) => Promise<PersonDto[]>;
};
