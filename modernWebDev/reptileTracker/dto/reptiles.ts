export interface CreateReptileBody {
  species: "ball_python" | "king_snake" | "corn_snake" | "redtail_boa";
  name: string;
  sex: "m" | "f";
}

export interface CreateFeedingBody {
  foodItem: string;
}

export type UpdateReptileBody = Partial<CreateReptileBody>;

export interface CreateHusbandryBody {
  length: number;
  weight: number;
  temperature: number;
  humidity: number;
}

export interface CreateScheduleBody {
  type: string;
  description: string;
  monday: boolean;
  tuesday: boolean;
  wednesday: boolean;
  thursday: boolean;
  friday: boolean;
  saturday: boolean;
  sunday: boolean;
}
