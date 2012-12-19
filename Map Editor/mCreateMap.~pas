unit mCreateMap;

interface

uses
  Windows, Messages, SysUtils, Variants, Classes, Graphics, Controls, Forms,
  Dialogs, StdCtrls, ComCtrls, XPMan, FileCtrl, ExtCtrls, Buttons;

type
  TCreateMap = class(TForm)
    btPath: TButton;
    leMapname: TLabeledEdit;
    lePath: TLabeledEdit;
    leChunksizeX: TLabeledEdit;
    leChunksizeY: TLabeledEdit;
    leChunksizeZ: TLabeledEdit;
    bbOK: TBitBtn;
    bbClose: TBitBtn;
    procedure btPathClick(Sender: TObject);
    procedure FormCreate(Sender: TObject);
    procedure leMapnameChange(Sender: TObject);
    procedure bbOKClick(Sender: TObject);
  private
    fAOwner: TComponent;
  public
    constructor create(AOwner: TComponent);
  end;

var
  CreateMap: TCreateMap;

implementation

uses mLeveleditor;

{$R *.dfm}

constructor TCreateMap.create(AOwner: TComponent);
begin
  inherited create(AOwner);
  fAOwner := AOwner;
end;

procedure TCreateMap.btPathClick(Sender: TObject);
const
     SELDIRHELP = 1000;
var
   dir: String;
begin
   dir := 'C:';
   if SelectDirectory(
        dir,
        [sdAllowCreate,
        sdPerformCreate,
        sdPrompt],
        SELDIRHELP
      ) then
     lePath.Text := dir;
end;


procedure TCreateMap.FormCreate(Sender: TObject);
begin
  lePath.Text := ExtractFilePath(Application.Exename);
end;

procedure TCreateMap.leMapnameChange(Sender: TObject);
begin
   leMapname.Text := StringReplace(leMapname.Text, ' ', '', [rfReplaceAll]);
end;

procedure TCreateMap.bbOKClick(Sender: TObject);
begin
    //if string name is ok
   if (leMapname.Text<>'') and (lePath.Text<>'') then begin
      if not DirectoryExists(lePath.Text+'\') then
         CreateDir(lePath.Text);
      if leMapname.Text[length(leMapname.Text)-1]<>'\' then
         Mapeditor := TMapeditor.Create(fAOwner,lePath.Text+'\', leMapname.Text)
      else Mapeditor := TMapeditor.Create(fAOwner,lePath.Text, leMapname.Text);
      Mapeditor.Show;
      inherited destroy;
   end else begin
      ShowMessage('You are a f*****g idiot! You are too stupid for this simple formula. Check your fields again!');
   end;
end;

end.
