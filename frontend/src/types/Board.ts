export interface SimpleBoardDTO {
  boardId: number;
  title: string;
  teamName: string;
  writeDate: string;
}

export interface SimpleCommentDTO {

  userId: number;
  title: string;
  content: string;
  writeDate: string;

}