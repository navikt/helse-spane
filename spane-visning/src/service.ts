import testPerson from "./resources/testPerson.json";
import {PersonDto} from "./types";

export const restBackend = (): Backend => {
    return {
        person(fnr: string): Promise<PersonDto> {
            return fetch(`/fnr/` + fnr, {
                method: "get",
                headers: {
                    Accept: "application/json",
                },
            }).then((response) => response.json());
        },
    };
};

export const testBackend = (): Backend => {
    return {
        person(): Promise<PersonDto> {
            return Promise.resolve(testPerson as unknown as PersonDto);
        },
    };
};

export type Backend = {
    person: (fnr: string) => Promise<PersonDto>;
};