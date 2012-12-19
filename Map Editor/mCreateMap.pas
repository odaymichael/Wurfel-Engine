unit mCreateMap;

interface

uses
  Windows, Messages, SysUtils, Variants, Classes, Graphics, Controls, Forms,
  Dialogs, StdCtrls, ComCtrls, XPMan, FileCtrl, ExtCtrls;

type
  TCreateMap = class(TForm)
    btOK: TButton;
    edPath: TEdit;
    btPath: TButton;
    laMapname: TLabeledEdit;
    procedure btPathClick(Sender: TObject);
    procedure btOKClick(Sender: TObject);
    procedure FormCreate(Sender: TObject);
    procedure laMapnameChange(Sender: TObject);
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
     edPath.Text := dir;
end;

procedure TCreateMap.btOKClick(Sender: TObject);
begin
   //if string name is ok
   if (laMapname.Text<>'') and (edPath.Text<>'') then begin
      if not DirectoryExists(edPath.Text+laMapname.Text+'\') then
         CreateDir(edPath.Text+laMapname.Text);
      Mapeditor := TMapeditor.Create(fAOwner,edPath.Text, laMapname.Text);
      Mapeditor.Show;
      inherited destroy;
   end else begin
      ShowMessage('You are a f*****g idiot! You are too stupid for this simple formula. Check your fields again!');
   end;
end;

procedure TCreateMap.FormCreate(Sender: TObject);
begin
  edPath.Text := ExtractFilePath(Application.Exename);
end;

procedure TCreateMap.laMapnameChange(Sender: TObject);
begin
   laMapname.Text := StringReplace(laMapname.Text, ' ', '', [rfReplaceAll]);
end;

end.
