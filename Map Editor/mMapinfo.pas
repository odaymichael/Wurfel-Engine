unit mMapinfo;

interface

uses
  Windows, Messages, SysUtils, Variants, Classes, Graphics, Controls, Forms,
  Dialogs, StdCtrls;

type
  TMapinfo = class(TForm)
    edMapname: TEdit;
    btSaveNew: TButton;
    laMapname: TLabel;
    edVersion: TEdit;
    laEditorversion: TLabel;
    edChunksizeX: TEdit;
    edChunksizeY: TEdit;
    laChunksize: TLabel;
    laChunksizeX: TLabel;
    laChunksizeY: TLabel;
    edChunksizeZ: TEdit;
    laChunksizeZ: TLabel;
    procedure btSaveNewClick(Sender: TObject);
    procedure FormClose(Sender: TObject; var Action: TCloseAction);
    procedure FormCreate(Sender: TObject);
    procedure SaveInfo();
  private
    
  public
         
  end;

var Mapinfo: TMapinfo;

implementation

uses mLeveleditor, mWelcome;

{$R *.dfm}

procedure TMapinfo.btSaveNewClick(Sender: TObject);
var i:integer;
begin
    if Mapeditor.SaveDialog.Execute then begin
       Mapeditor.mappath := Mapeditor.SaveDialog.FileName;
       for i:=Length(Mapeditor.mappath) downto 1 do
          if Mapeditor.mappath[i] = '\' then begin
             Mapeditor.mappath := Copy(Mapeditor.mappath,1,i);
             break;
          end;
       SaveInfo();
    end
    else ShowMessage('Du hast abgebrochen. Das war ein fataler Fehler. Festplatte wird formatiert!'+
    #13#10+'-Ende der schlechten Witze..');
end;

procedure TMapinfo.SaveInfo();
var Stream: TFilestream;
    Writer: TWriter;
begin
   Stream := TFilestream.Create(Mapeditor.mappath+'map.otmi',fmCreate);
   Writer := TWriter.Create(Stream,500);
   Mapeditor.mapname := edMapname.Text;
   Writer.WriteString(edMapname.Text+#13#10);
   Writer.WriteString(edVersion.Text+#13#10);
   Writer.WriteString('0,0'+#13#10);
   Writer.Free;
   Stream.Free;
end;

procedure TMapinfo.FormClose(Sender: TObject; var Action: TCloseAction);
begin
   SaveInfo();
end;

procedure TMapinfo.FormCreate(Sender: TObject);
begin
   edMapname.Text := Mapeditor.mapname;
   edVersion.Text := Welcome.getVersion;
   edChunksizeX.Text := IntToStr(Mapeditor.ChunkSizeX);
   edChunksizeY.Text := IntToStr(Mapeditor.ChunkSizeY);
   edChunksizeZ.Text := IntToStr(Mapeditor.ChunkSizeZ);
end;

end.
